package com.oberasoftware.robo.dynamixel.commands;

import com.oberasoftware.robo.api.servo.ServoCommand;

/**
 * @author renarj
 */
public class DynamixelReadAngleLimit implements ServoCommand {

    private final String servoId;

    public DynamixelReadAngleLimit(String servoId) {
        this.servoId = servoId;
    }

    @Override
    public String getServoId() {
        return servoId;
    }

    @Override
    public String toString() {
        return "DynamixelReadAngleLimit{" +
                "servoId='" + servoId + '\'' +
                '}';
    }
}
