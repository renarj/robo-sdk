package com.oberasoftware.robo.core.commands;

import com.oberasoftware.robo.api.servo.ServoCommand;

/**
 * @author renarj
 */
public class ReadAngleLimit implements ServoCommand {

    private final String servoId;

    public ReadAngleLimit(String servoId) {
        this.servoId = servoId;
    }

    @Override
    public String getServoId() {
        return servoId;
    }

    @Override
    public String toString() {
        return "ReadAngleLimit{" +
                "servoId='" + servoId + '\'' +
                '}';
    }
}
