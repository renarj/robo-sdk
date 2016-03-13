package com.oberasoftware.robo.api.commands;

import com.oberasoftware.robo.api.ServoCommand;

/**
 * @author Renze de Vries
 */
public class PositionCommand implements ServoCommand {
    private final String servoId;
    private final int position;

    public PositionCommand(String servoId, int position) {
        this.servoId = servoId;
        this.position = position;
    }

    @Override
    public String getServoId() {
        return servoId;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "PositionCommand{" +
                "servoId=" + servoId +
                ", position=" + position +
                '}';
    }
}
