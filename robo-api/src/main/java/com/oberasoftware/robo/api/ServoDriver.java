package com.oberasoftware.robo.api;

import com.oberasoftware.robo.api.commands.PositionAndSpeedCommand;

import java.util.Map;

/**
 * @author Renze de Vries
 */
public interface ServoDriver {
    boolean setServoSpeed(String servoId, int speed);

    boolean setTargetPosition(String servoId, int targetPosition);

    boolean setPositionAndSpeed(String servoId, int speed, int targetPosition);

    boolean bulkSetPositionAndSpeed(Map<String, PositionAndSpeedCommand> commands);
}
