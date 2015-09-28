package com.oberasoftware.robo.core.commands;


import com.oberasoftware.robo.api.ServoCommand;

/**
 * @author Renze de Vries
 */
public class PositionAndSpeedCommand implements ServoCommand {
    private final String servoId;
    private final int position;
    private final int speed;

    public PositionAndSpeedCommand(String servoId, int position, int speed) {
        this.servoId = servoId;
        this.position = position;
        this.speed = speed;
    }

    @Override
    public String getServoId() {
        return servoId;
    }

    public int getPosition() {
        return position;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return "PositionAndSpeedCommand{" +
                "servoId='" + servoId + '\'' +
                ", position=" + position +
                ", speed=" + speed +
                '}';
    }
}
