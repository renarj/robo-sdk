package com.oberasoftware.robo.core.commands;

import com.oberasoftware.robo.api.servo.ServoCommand;

public class RebootCommand implements ServoCommand {
    private final String servoId;

    public RebootCommand(String servoId) {
        this.servoId = servoId;
    }

    @Override
    public String getServoId() {
        return servoId;
    }

    @Override
    public String toString() {
        return "RebootCommand{" +
                "servoId='" + servoId + '\'' +
                '}';
    }
}
