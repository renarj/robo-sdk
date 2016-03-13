package com.oberasoftware.robo.api.commands;

import com.oberasoftware.robo.api.ServoCommand;

/**
 * @author Renze de Vries
 */
public class SpeedCommand implements ServoCommand {
    private final String servoId;
    private final int speed;

    public SpeedCommand(String servoId, int speed) {
        this.servoId = servoId;
        this.speed = speed;
    }

    @Override
    public String getServoId() {
        return servoId;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return "SpeedCommand{" +
                "servoId='" + servoId + '\'' +
                ", speed=" + speed +
                '}';
    }
}
