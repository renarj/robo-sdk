package com.oberasoftware.robo.api.commands;


import com.oberasoftware.robo.api.servo.ServoCommand;

/**
 * @author Renze de Vries
 */
public class PositionAndSpeedCommand implements ServoCommand {
    private final String servoId;
    private final int position;
    private final int speed;
    private final Scale speedScale;
    private final Scale positionScale;

    public PositionAndSpeedCommand(String servoId, int position, Scale positionScale, int speed, Scale speedScale) {
        this.servoId = servoId;
        this.position = position;
        this.speed = speed;
        this.speedScale = speedScale;
        this.positionScale = positionScale;
    }

    public Scale getSpeedScale() {
        return speedScale;
    }

    public Scale getPositionScale() {
        return positionScale;
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
