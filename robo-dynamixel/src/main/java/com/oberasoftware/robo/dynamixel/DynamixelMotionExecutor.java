package com.oberasoftware.robo.dynamixel;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.Uninterruptibles;
import com.oberasoftware.base.event.EventBus;
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

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Renze de Vries
 */
@Component
public class DynamixelMotionExecutor implements MotionExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelMotionExecutor.class);

    @Autowired
    private EventBus eventBus;

    @Autowired
    private ServoDataManager dataManager;

    @Override
    public void execute(Motion motion) {
        //no repeats exeternally specified, use defaults from the motion

        execute(motion, motion.getRepeats());
    }

    @Override
    public void execute(Motion motion, int repeats) {
        LOG.debug("Executing motion: {} repeats: {}", motion, repeats);

        int amount = repeats + 1; //repeats are 0 based, but we always execute at least once
        for(int i=0; i<amount; i++) {
            LOG.debug("Motion: {} execution round: {}", motion.getName(), (i + i));
            List<Step> steps = motion.getSteps();

            for(int c=0; c<steps.size(); c++) {
                Stopwatch stopwatch = Stopwatch.createStarted();
                LOG.debug("Executing step: {}", c);

                Step step = steps.get(c);
                Step previousStep = null;
                if(c > 0) {
                    previousStep = steps.get(c - 1);
                } else if(i > 0) {
                    //if we are doing a repeat its simply last step of the motion
                    previousStep = steps.get(steps.size() - 1);
                }

                executeStep(previousStep, step);

                LOG.debug("Finished step: {} execution in: {} ms.", c, stopwatch.elapsed(TimeUnit.MILLISECONDS));
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
        double timeInSeconds = (double)timeInMs / 1000;
        double rotationsPerSec = rotationsNeeded / timeInSeconds;

        int speed = (int)(rotationsPerSec / unitRotationsPerSecond);
        LOG.trace("Required speed: {}", speed);

        return speed;
    }
}
