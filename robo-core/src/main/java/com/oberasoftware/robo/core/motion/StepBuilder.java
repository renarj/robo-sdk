package com.oberasoftware.robo.core.motion;

import com.oberasoftware.robo.api.motion.KeyFrame;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Renze de Vries
 */
public class StepBuilder {

    private int commonSpeed = -1;
    private long timeInMs;
    private String frameId;
    private String motionId;
    
    private final List<ServoStepImpl> servoSteps = new ArrayList<>();

    private StepBuilder(String motionId, String frameId, long timeInMs) {
        this.motionId = motionId;
        this.timeInMs = timeInMs;
        this.frameId = frameId;
    }

    public static StepBuilder create(String motionId, String frameId, long timeInMs) {
        return new StepBuilder(motionId, frameId, timeInMs);
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

    public KeyFrame build() {
        KeyFrameImpl step = new KeyFrameImpl(motionId, frameId, timeInMs);
        servoSteps.forEach(step::addServoStep);

        return step;
    }
}
