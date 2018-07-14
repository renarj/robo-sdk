package com.oberasoftware.robo.api.commands;

import com.oberasoftware.robo.api.servo.ServoCommand;

public class WriteIDCommand implements ServoCommand {
    private final String oldId;
    private final String newId;

    public WriteIDCommand(String oldId, String newId) {
        this.oldId = oldId;
        this.newId = newId;
    }

    @Override
    public String getServoId() {
        return oldId;
    }

    public String getOldId() {
        return oldId;
    }

    public String getNewId() {
        return newId;
    }

    @Override
    public String toString() {
        return "WriteIDCommand{" +
                "oldId='" + oldId + '\'' +
                ", newId='" + newId + '\'' +
                '}';
    }
}
