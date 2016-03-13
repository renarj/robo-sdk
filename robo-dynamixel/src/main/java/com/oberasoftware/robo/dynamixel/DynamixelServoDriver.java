package com.oberasoftware.robo.dynamixel;

import com.oberasoftware.robo.api.ServoDriver;
import com.oberasoftware.robo.api.commands.PositionAndSpeedCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Renze de Vries
 */
@Component
public class DynamixelServoDriver implements ServoDriver {

    @Autowired
    private SerialDynamixelConnector connector;

    private String portName;

    public DynamixelServoDriver(String portName) {
        this.portName = portName;
    }

    @Override
    public void activate() {
        connector.connect(portName);
    }

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
