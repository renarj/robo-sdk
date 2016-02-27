package com.oberasoftware.robo.dynamixel;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.Uninterruptibles;
import com.oberasoftware.base.event.EventBus;
import com.oberasoftware.robo.api.MotionManager;
import com.oberasoftware.robo.api.ServoDataManager;
import com.oberasoftware.robo.api.ServoProperty;
import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.api.motion.MotionExecutor;
import com.oberasoftware.robo.api.motion.ServoStep;
import com.oberasoftware.robo.api.motion.Step;
import com.oberasoftware.robo.core.commands.BulkPositionSpeedCommand;
import com.oberasoftware.robo.core.commands.PositionAndSpeedCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

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
                    Uninterruptibles.sleepUninterruptibly(INTERVAL, TimeUnit.MILLISECONDS);
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
        runMotion(motion, null, repeats);
    }

    private void runMotion(Motion motion, Step lastExecutedStep, int repeats) {
        LOG.debug("Executing motion: {} repeats: {}", motion, repeats);


        int amount = repeats + 1; //repeats are 0 based, but we always execute at least once
        for(int i=0; i<amount; i++) {
            LOG.info("Motion: {} execution round: {}", motion.getName(), (i + i));
            List<Step> steps = motion.getSteps();

            Stopwatch motionWatch = Stopwatch.createStarted();
            for(int c=0; c<steps.size(); c++) {
                Stopwatch stopwatch = Stopwatch.createStarted();
                LOG.debug("Executing step: {}", c);

                Step step = steps.get(c);
                Step previousStep = lastExecutedStep;
                if(c > 0) {
                    previousStep = steps.get(c - 1);
                } else if(i > 0) {
                    //if we are doing a repeat its simply last step of the motion
                    previousStep = steps.get(steps.size() - 1);
                }

                executeStep(previousStep, step);

                LOG.info("Finished step: {} execution in: {} ms.", c, stopwatch.elapsed(TimeUnit.MILLISECONDS));
            }
            LOG.info("Finished motion: {} execution in: {} ms.", motion.getName(), motionWatch.elapsed(TimeUnit.MILLISECONDS));
            checkAndExceuteNextMotion(motion);

        }
    }

    private void checkAndExceuteNextMotion(Motion currentMotion) {
        if(loopCount.get() < 10) {
            LOG.info("Loopcount under 10");
            String nextMotionId = currentMotion.getNextMotion();
            LOG.info("Execution next motion: {}", nextMotionId);
            if (nextMotionId != null) {
                Optional<Motion> nextMotion = motionManager.findMotionById(nextMotionId);
                if (nextMotion.isPresent()) {
                    LOG.info("A chain of motions is present, executing next motion: {} ({})", nextMotionId, nextMotion.get().getName());
                    loopCount.incrementAndGet();
                    List<Step> lastSteps = currentMotion.getSteps();
                    Step lastStep = lastSteps.get(lastSteps.size() - 1);

                    runMotion(nextMotion.get(), lastStep, 0);
                } else {
                    LOG.warn("Next motion: {} could not be found", nextMotionId);
                }
            }
        } else {
            String exitMotionId = currentMotion.getExitMotion();
            LOG.info("Execution exit motion: {}", exitMotionId);
            if (exitMotionId != null) {
                Optional<Motion> exitMotion = motionManager.findMotionById(exitMotionId);
                if (exitMotion.isPresent()) {
                    List<Step> lastSteps = currentMotion.getSteps();
                    Step lastStep = lastSteps.get(lastSteps.size() - 1);

                    runMotion(exitMotion.get(), lastStep, 0);
                } else {
                    LOG.warn("Exit motion: {} could not be found", exitMotionId);
                }
            }

        }
    }

    private void executeStep(Step previousStep, Step step) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Map<String, PositionAndSpeedCommand> commands = step.getServoSteps().stream()
                .map(s -> new PositionAndSpeedCommand(s.getServoId(), s.getTargetPosition(),
                        calculateSpeed(previousStep, s.getServoId(), s.getTargetPosition(), step.getTimeInMs())))
                .collect(Collectors.toMap(PositionAndSpeedCommand::getServoId, Function.identity()));
        long duration = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        if(duration > 50) {
            LOG.warn("It took a long time to calculate diff: {} ms.", duration);
        }
        LOG.debug("Executing step: {} calculation in: {} ms.", step, duration);
        eventBus.publish(new BulkPositionSpeedCommand(commands));

        Uninterruptibles.sleepUninterruptibly(step.getTimeInMs(), TimeUnit.MILLISECONDS);
    }

    private int calculateSpeed(Step previousStep, String servoId, int targetPosition, long timeInMs) {
        int currentPosition;
        if(previousStep != null) {
            ServoStep previousServoStep = previousStep.getServoSteps().stream().filter(s -> s.getServoId().equals(servoId)).findFirst().get();
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
        double timeInSeconds = ((double)timeInMs / 1000) / 1.2;
        double rotationsPerSec = rotationsNeeded / timeInSeconds;

        int speed = (int)(rotationsPerSec / unitRotationsPerSecond);
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
