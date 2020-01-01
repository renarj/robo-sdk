package com.oberasoftware.robo.core.motion;

import com.oberasoftware.robo.api.motion.JointTarget;

/**
 * @author Renze de Vries
 */
public class JointTargetImpl implements JointTarget {

    private String servoId;
    private int targetPosition;
    private int targetAngle = 0;
    private int targetVelocity = 0;

    public JointTargetImpl(String servoId, int targetPosition, int targetAngle) {
        this.servoId = servoId;
        this.targetPosition = targetPosition;
        this.targetAngle = targetAngle;
    }

    public JointTargetImpl(String servoId, int targetPosition) {
        this.servoId = servoId;
        this.targetPosition = targetPosition;
    }

    public JointTargetImpl() {
    }

    @Override
    public int getTargetAngle() {
        return targetAngle;
    }

    public void setTargetAngle(int targetAngle) {
        this.targetAngle = targetAngle;
    }

    @Override
    public String getServoId() {
        return servoId;
    }

    public void setServoId(String servoId) {
        this.servoId = servoId;
    }

    @Override
    public int getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(int targetPosition) {
        this.targetPosition = targetPosition;
    }

    @Override
    public int getTargetVelocity() {
        return targetVelocity;
    }

    public void setTargetVelocity(int targetVelocity) {
        this.targetVelocity = targetVelocity;
    }

    @Override
    public String toString() {
        return "ServoStepImpl{" +
                "servoId='" + servoId + '\'' +
                ", targetPosition=" + targetPosition +
                '}';
    }
}
