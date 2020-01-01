package com.oberasoftware.robo.api.motion;

/**
 * @author Renze de Vries
 */
public interface JointTarget {
    String getServoId();

    int getTargetPosition();

    int getTargetAngle();

    int getTargetVelocity();
}
