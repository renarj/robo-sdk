package com.oberasoftware.robo.core.commands;

import com.oberasoftware.robo.api.servo.ServoCommand;

public class CurrentLimitCommand implements ServoCommand  {
    private final String servoId;
    private final int limit;

    public CurrentLimitCommand(String servoId, int limit) {
        this.servoId = servoId;
        this.limit = limit;
    }

    @Override
    public String getServoId() {
        return servoId;
    }

    public int getLimit() {
        return limit;
    }

    @Override
    public String toString() {
        return "CurrentLimitCommand{" +
                "servoId='" + servoId + '\'' +
                ", limit=" + limit +
                '}';
    }
}
