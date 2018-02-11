package com.oberasoftware.robo.dynamixel;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Uninterruptibles;
import com.oberasoftware.base.event.impl.LocalEventBus;
import com.oberasoftware.robo.api.MotionTask;
import com.oberasoftware.robo.api.commands.BulkPositionSpeedCommand;
import com.oberasoftware.robo.api.commands.PositionAndSpeedCommand;
import com.oberasoftware.robo.api.motion.*;
import com.oberasoftware.robo.api.servo.ServoDataManager;
import com.oberasoftware.robo.api.servo.ServoProperty;
import com.oberasoftware.robo.core.motion.MotionImpl;
import com.oberasoftware.robo.dynamixel.handlers.DynamixelSyncWriteMovementHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
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
    private LocalEventBus eventBus;

    @Autowired
    private ServoDataManager dataManager;

    @Autowired
    private MotionManager motionManager;

    @Autowired
    private DynamixelSyncWriteMovementHandler syncWriteMovementHandler;

    private Queue<MotionTaskImpl> queue = new LinkedBlockingQueue<>();
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean running;

    private Map<String, BulkPositionSpeedCommand> cachedCommands = new HashMap<>();

    @PostConstruct
    public void startListener() {
        running = true;
        executor.execute(() -> {
            LOG.info("Starting motion queue");
            while(running && !Thread.currentThread().isInterrupted()) {
                MotionTaskImpl item = queue.poll();
                if(item != null && !item.isCancelled()) {
                    try {
                        runMotion(item, null);
                    } finally {
                        item.complete();
                    }
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
    public MotionTask execute(Motion motion) {
        MotionTaskImpl motionTask = new MotionTaskImpl(motion);

        LOG.debug("Scheduling motion in the queue: {}", motion);
        queue.add(motionTask);

        return motionTask;
    }

    private void runMotion(MotionTask motionTask, KeyFrame lastExecutedKeyFrame) {
        LOG.info("Executing motion task: {}", motionTask);
        motionTask.start();

        Motion motion = motionTask.getMotion();
        KeyFrame lastKeyFrame = lastExecutedKeyFrame;
        while(motion != null) {
            lastKeyFrame = executeMotion(motion, lastKeyFrame);

            motion = getNextChainedMotion(motion, !motionTask.isRunning());
        }
    }

    @Override
    public MotionTask execute(KeyFrame keyFrame) {
        String motionId = UUID.randomUUID().toString();
        return execute(new MotionImpl(motionId, motionId, null, null, Lists.newArrayList(keyFrame)));
    }

    private KeyFrame executeMotion(Motion motion, KeyFrame previousKeyFrame) {
        LOG.info("Motion: {} execution", motion.getName());
        List<KeyFrame> keyFrames = motion.getKeyFrames();
        KeyFrame lastKeyFrame = previousKeyFrame;
        Stopwatch motionWatch = createStarted();
        for (int c = 0; c < keyFrames.size(); c++) {
            Stopwatch stopwatch = createStarted();
            LOG.debug("Executing keyFrame: {}", c);

            KeyFrame keyFrame = keyFrames.get(c);

            executeKeyFrame(motion.getId(), lastKeyFrame, keyFrame);
            lastKeyFrame = keyFrame;

            LOG.info("Finished keyFrame: {} execution in: {} ms. target time: {}", c,
                    stopwatch.elapsed(MILLISECONDS), keyFrame.getTimeInMs());
        }
        LOG.info("Finished motion: {} execution in: {} ms.", motion.getName(), motionWatch.elapsed(MILLISECONDS));
        return lastKeyFrame;
    }

    private Motion getNextChainedMotion(Motion currentMotion, boolean exitMotion) {
        String nextMotionId = currentMotion.getNextMotion();
        if(exitMotion) {
            nextMotionId = currentMotion.getExitMotion();
        }
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

    private void executeKeyFrame(String motionId, KeyFrame previousKeyFrame, KeyFrame keyFrame) {
        long timeInMs = keyFrame.getTimeInMs();

        String previousFrameId = previousKeyFrame != null ? previousKeyFrame.getKeyFrameId() : "";
        String cacheKey = motionId + "_" + previousFrameId + "_" + keyFrame.getKeyFrameId();
        if(!cachedCommands.containsKey(cacheKey)) {
            Map<String, PositionAndSpeedCommand> commands = keyFrame.getServoSteps().stream()
                    .map(s -> new PositionAndSpeedCommand(s.getServoId(), s.getTargetPosition(),
                            calculateSpeed(previousKeyFrame, s.getServoId(), s.getTargetPosition(), timeInMs)))
                    .collect(Collectors.toMap(PositionAndSpeedCommand::getServoId, Function.identity()));
            cachedCommands.put(cacheKey, new BulkPositionSpeedCommand(commands));
        }

        Stopwatch stopwatch = createStarted();
        BulkPositionSpeedCommand bulkPositionSpeedCommand = cachedCommands.get(cacheKey);
        syncWriteMovementHandler.receive(bulkPositionSpeedCommand);

        long s = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        LOG.debug("Finished sending: {} in {} ms.", bulkPositionSpeedCommand, s);

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
//        speed = speed * 2;
        LOG.trace("Required speed: {}", speed);

        return speed;
    }

    private class MotionTaskImpl implements MotionTask {

        private final Motion motion;
        private final String taskId = UUID.randomUUID().toString();
        private final AtomicBoolean running = new AtomicBoolean(false);
        private final AtomicBoolean cancelled = new AtomicBoolean(false);
        private final CountDownLatch latch = new CountDownLatch(1);

        public MotionTaskImpl(Motion motion) {
            this.motion = motion;
        }

        @Override
        public String getTaskId() {
            return taskId;
        }

        @Override
        public Motion getMotion() {
            return this.motion;
        }

        @Override
        public boolean isCancelled() {
            return cancelled.get();
        }

        @Override
        public boolean isRunning() {
            return running.get() && !isCancelled();
        }

        @Override
        public void cancel() {
            running.set(false);
            cancelled.set(true);
        }

        @Override
        public void start() {
            this.running.set(true);
        }

        private void complete() {
            latch.countDown();
        }

        @Override
        public void awaitCompletion() {
            Uninterruptibles.awaitUninterruptibly(latch);
        }
    }
}
