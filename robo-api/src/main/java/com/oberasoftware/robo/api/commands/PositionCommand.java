package com.oberasoftware.robo.api.commands;

import com.oberasoftware.robo.api.servo.ServoCommand;

/**
 * @author Renze de Vries
 */
public class PositionCommand implements ServoCommand {
    private final String servoId;
    private final int position;
    private final Scale scale;

    public PositionCommand(String servoId, int position, Scale scale) {
        this.servoId = servoId;
        this.position = position;
        this.scale = scale;
    }

    public Scale getScale() {
        return scale;
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
