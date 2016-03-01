package com.oberasoftware.robo.dynamixel;

import com.google.common.base.Stopwatch;
import com.oberasoftware.base.event.EventBus;
import com.oberasoftware.robo.api.MotionManager;
import com.oberasoftware.robo.api.ServoDataManager;
import com.oberasoftware.robo.api.ServoProperty;
import com.oberasoftware.robo.api.motion.KeyFrame;
import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.api.motion.MotionExecutor;
import com.oberasoftware.robo.api.motion.ServoStep;
import com.oberasoftware.robo.core.commands.BulkPositionSpeedCommand;
import com.oberasoftware.robo.core.commands.PositionAndSpeedCommand;
import com.oberasoftware.robo.dynamixel.handlers.DynamixelSyncWriteMovementHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Stopwatch.createStarted;
import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author Renze de Vries
 */
@Component
public class DynamixelMotionExecutor implements MotionExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelMotionExecutor.class);

    private static final int INTERVAL = 1000;

    @Autowired
    private EventBus eventBus;

    @Autowired
    private ServoDataManager dataManager;

    @Autowired
    private MotionManager motionManager;

    @Autowired
    private DynamixelSyncWriteMovementHandler syncWriteMovementHandler;

    private Queue<QueueItem> queue = new LinkedBlockingQueue<>();
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean running;

    private AtomicInteger loopCount = new AtomicInteger(0);

    @PostConstruct
    public void startListener() {
        running = true;
        executor.execute(() -> {
            LOG.info("Starting motion queue");
            while(running && !Thread.currentThread().isInterrupted()) {
                QueueItem item = queue.poll();
                if(item != null) {
                    runMotion(item);
                } else {
                    sleepUninterruptibly(INTERVAL, MILLISECONDS);
                }
            }
            LOG.info("Motion queue has stopped");
        });
    }

    @PreDestroy
    public void stopListener() {
        LOG.debug("Shutting down motion queue");
        running = false;
        executor.shutdownNow();
    }

    @Override
    public void execute(Motion motion) {
        execute(motion, motion.getRepeats());
    }

    @Override
    public void execute(Motion motion, int repeats) {
        LOG.debug("Scheduling motion in the queue: {} repeats: {}", motion, repeats);
        loopCount.set(0);
        queue.add(new QueueItem(motion, repeats));
    }

    private void runMotion(QueueItem item) {
        runMotion(item.getMotion(), item.getRepeats());
    }

    private void runMotion(Motion motion, int repeats) {
        runSingleMotion(motion, null, repeats, true);
    }

    private void runSingleMotion(Motion motion, KeyFrame lastExecutedKeyFrame, int repeats, boolean executeExitMotion) {
        LOG.info("Executing motion: {} repeats: {}", motion, repeats);

        Motion currentMotion = motion;
        int amount = repeats + 1; //repeats are 0 based, but we always execute at least once
        int i = 0;
        Motion lastExecuted = null;
        KeyFrame previousKeyFrame = lastExecutedKeyFrame;
        while(currentMotion != null && i < amount) {

            LOG.info("Motion: {} execution round: {}", currentMotion.getName(), (i + 1));
            List<KeyFrame> keyFrames = currentMotion.getKeyFrames();

            Stopwatch motionWatch = createStarted();
            for (int c = 0; c < keyFrames.size(); c++) {
                Stopwatch stopwatch = createStarted();
                LOG.debug("Executing keyFrame: {}", c);

                KeyFrame keyFrame = keyFrames.get(c);

                if (c > 0) {
                    previousKeyFrame = keyFrames.get(c - 1);
                } else if (i > 0) {
                    //if we are doing a repeat its simply last keyFrame of the motion
                    previousKeyFrame = keyFrames.get(keyFrames.size() - 1);
                }

                executeKeyFrame(previousKeyFrame, keyFrame);

                LOG.info("Finished keyFrame: {} execution in: {} ms. target time: {}", c,
                        stopwatch.elapsed(MILLISECONDS), keyFrame.getTimeInMs());
            }
            LOG.info("Finished motion: {} execution in: {} ms.", currentMotion.getName(), motionWatch.elapsed(MILLISECONDS));
            lastExecuted = currentMotion;
            currentMotion = getNextChainedMotion(currentMotion);

            i++;
        }

        LOG.info("Going to execute exit motion for motion: {}", lastExecuted);
        Motion exitMotion = getExitMotion(lastExecuted);
        if(exitMotion != null && executeExitMotion) {
            LOG.info("Lets run it baby");
            runSingleMotion(exitMotion, null, 0, false);
        }
    }

    private Motion getExitMotion(Motion currentMotion) {
        String nextMotionId = currentMotion.getExitMotion();
        return findMotion(nextMotionId);
    }

    private Motion getNextChainedMotion(Motion currentMotion) {
        String nextMotionId = currentMotion.getNextMotion();
        return findMotion(nextMotionId);
    }

    private Motion findMotion(String motionId) {
        if (motionId != null) {
            Optional<Motion> motion = motionManager.findMotionById(motionId);
            if (motion.isPresent()) {
                return motion.get();
            } else {
                LOG.debug("Motion: {} could not be found", motionId);
            }
        }
        return null;

    }

    private void executeKeyFrame(KeyFrame previousKeyFrame, KeyFrame keyFrame) {
        long timeInMs = keyFrame.getTimeInMs();

        Map<String, PositionAndSpeedCommand> commands = keyFrame.getServoSteps().stream()
                .map(s -> new PositionAndSpeedCommand(s.getServoId(), s.getTargetPosition(),
                        calculateSpeed(previousKeyFrame, s.getServoId(), s.getTargetPosition(), timeInMs)))
                .collect(Collectors.toMap(PositionAndSpeedCommand::getServoId, Function.identity()));

        Stopwatch stopwatch = createStarted();
        syncWriteMovementHandler.receive(new BulkPositionSpeedCommand(commands));

        //sleep minus the time it took to write to the bus
        sleepUninterruptibly(timeInMs - stopwatch.elapsed(MILLISECONDS), MILLISECONDS);
    }

    private int calculateSpeed(KeyFrame previousKeyFrame, String servoId, int targetPosition, long timeInMs) {
        int currentPosition;
        if(previousKeyFrame != null) {
            ServoStep previousServoStep = previousKeyFrame.getServoSteps().stream().filter(s -> s.getServoId().equals(servoId)).findFirst().get();
            currentPosition = previousServoStep.getTargetPosition();
        } else {
            currentPosition = dataManager.readServoProperty(servoId, ServoProperty.POSITION);
        }


        return calculateRotations(currentPosition, targetPosition, timeInMs);
    }

    public static int calculateRotations(int currentPosition, int targetPosition, long timeInMs) {
        double unitRotationsPerSecond = (0.111 / 60);
        int delta = Math.abs(targetPosition - currentPosition);
        double rotationsNeeded = ((double)delta / 1023);
        double timeInSeconds = ((double)timeInMs / 1000);
        double rotationsPerSec = rotationsNeeded / timeInSeconds;

        int speed = (int)(rotationsPerSec / unitRotationsPerSecond);
        speed = speed * 2;
        LOG.trace("Required speed: {}", speed);

        return speed;
    }

    private class QueueItem {
        private Motion motion;
        private int repeats;

        public QueueItem(Motion motion, int repeats) {
            this.motion = motion;
            this.repeats = repeats;
        }

        public Motion getMotion() {
            return motion;
        }

        public int getRepeats() {
            return repeats;
        }
    }
}
