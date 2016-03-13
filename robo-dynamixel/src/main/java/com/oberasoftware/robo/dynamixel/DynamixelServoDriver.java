package com.oberasoftware.robo.dynamixel;

import com.oberasoftware.robo.api.ServoDriver;
import com.oberasoftware.robo.api.commands.PositionAndSpeedCommand;

import java.util.Map;

/**
 * @author Renze de Vries
 */
public class DynamixelServoDriver implements ServoDriver {
    @Override
    public boolean setServoSpeed(String servoId, int speed) {
        return false;
    }

    @Override
    public boolean setTargetPosition(String servoId, int targetPosition) {
        return false;
    }

    @Override
    public boolean setPositionAndSpeed(String servoId, int speed, int targetPosition) {
        return false;
    }

    @Override
    public boolean bulkSetPositionAndSpeed(Map<String, PositionAndSpeedCommand> commands) {
        return false;
    }
}
