package com.oberasoftware.robo.core.motion;

import com.oberasoftware.robo.api.motion.ServoStep;

/**
 * @author Renze de Vries
 */
public class ServoStepImpl implements ServoStep {

    private final String servoId;
    private final int targetPosition;
    private int speed;

    public ServoStepImpl(String servoId, int targetPosition, int speed) {
        this.servoId = servoId;
        this.targetPosition = targetPosition;
        this.speed = speed;
    }

    @Override
    public String getServoId() {
        return servoId;
    }

    @Override
    public int getTargetPosition() {
        return targetPosition;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "ServoStepImpl{" +
                "servoId='" + servoId + '\'' +
                ", targetPosition=" + targetPosition +
                ", speed=" + speed +
                '}';
    }
}
