package com.oberasoftware.robo.api.motion;

/**
 * @author Renze de Vries
 */
public interface ServoStep {
    String getServoId();

    int getTargetPosition();

    int getSpeed();
}
