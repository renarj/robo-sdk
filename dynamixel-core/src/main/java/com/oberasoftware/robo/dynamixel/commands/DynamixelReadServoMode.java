package com.oberasoftware.robo.dynamixel.commands;

import com.oberasoftware.robo.api.servo.ServoCommand;

/**
 * @author renarj
 */
public class DynamixelReadServoMode implements ServoCommand {

    private final String servoId;

    public DynamixelReadServoMode(String servoId) {
        this.servoId = servoId;
    }

    @Override
    public String getServoId() {
        return servoId;
    }

    @Override
    public String toString() {
        return "DynamixelReadServoMode{" +
                "servoId='" + servoId + '\'' +
                '}';
    }
}
