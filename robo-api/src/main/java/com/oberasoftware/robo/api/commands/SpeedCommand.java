package com.oberasoftware.robo.api.commands;

import com.oberasoftware.robo.api.servo.ServoCommand;

/**
 * @author Renze de Vries
 */
public class SpeedCommand implements ServoCommand {
    private final String servoId;
    private final int speed;
    private final Scale scale;

    public SpeedCommand(String servoId, int speed, Scale scale) {
        this.servoId = servoId;
        this.speed = speed;
        this.scale = scale;
    }

    public Scale getScale() {
        return scale;
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
