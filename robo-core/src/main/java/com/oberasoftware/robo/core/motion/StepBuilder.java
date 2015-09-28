package com.oberasoftware.robo.core.motion;

import com.oberasoftware.robo.api.motion.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Renze de Vries
 */
public class StepBuilder {

    private int commonSpeed = -1;
    private long timeInMs;
    
    private final List<ServoStepImpl> servoSteps = new ArrayList<>();

    private StepBuilder(long timeInMs) {
        this.timeInMs = timeInMs;
    }

    public static StepBuilder create(long timeInMs) {
        return new StepBuilder(timeInMs);
    }

    public StepBuilder servo(String servoId, int goalPosition, int speed) {
        this.servoSteps.add(new ServoStepImpl(servoId, goalPosition, speed));
        return this;
    }

    public StepBuilder servo(String servoId, int goalPosition) {
        this.servoSteps.add(new ServoStepImpl(servoId, goalPosition, commonSpeed));
        return this;
    }

    public StepBuilder speed(int speed) {
        servoSteps.forEach(s -> s.setSpeed(speed));
        return this;
    }

    public Step build() {
        StepImpl step = new StepImpl(timeInMs);
        servoSteps.forEach(step::addServoStep);

        return step;
    }
}
