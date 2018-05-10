package com.oberasoftware.robo.core.commands;

import com.oberasoftware.robo.api.servo.ServoCommand;

public class OperationModeCommand implements ServoCommand {
    public enum MODE {
        VELOCITY_MODE,
        POSITION_CONTROL
    }

    private final String servoId;
    private final MODE operationMode;

    public OperationModeCommand(String servoId, MODE operationMode) {
        this.servoId = servoId;
        this.operationMode = operationMode;
    }

    @Override
    public String getServoId() {
        return servoId;
    }

    public MODE getOperationMode() {
        return operationMode;
    }

    @Override
    public String toString() {
        return "OperationModeCommand{" +
                "servoId='" + servoId + '\'' +
                ", operationMode=" + operationMode +
                '}';
    }
}
