package com.oberasoftware.robo.core.commands;

import com.oberasoftware.robo.api.servo.ServoCommand;

public class VelocityModeCommand implements ServoCommand {

    public static final int DEFAULT_ACCELERATION = 15000;
    public static final int DEFAULT_VELOCITY = 15000;

    public enum VELOCITY_MODE {
        STEP,
        RECTANGLE,
        TRAPEZOID
    }

    private final String servoId;
    private final VELOCITY_MODE mode;

    private final int velocity;
    private final int acceleration;

    public VelocityModeCommand(String servoId, VELOCITY_MODE mode) {
        this(servoId, mode, DEFAULT_VELOCITY, DEFAULT_ACCELERATION);
    }

    public VelocityModeCommand(String servoId, int velocity, int acceleration) {
        this(servoId, determineMode(velocity, acceleration), velocity, acceleration);
    }

    private VelocityModeCommand(String servoId, VELOCITY_MODE mode, int velocity, int acceleration) {
        this.servoId = servoId;
        this.mode = mode;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    @Override
    public String getServoId() {
        return servoId;
    }

    public VELOCITY_MODE getMode() {
        return mode;
    }

    public int getVelocity() {
        return velocity;
    }

    public int getAcceleration() {
        return acceleration;
    }

    private static VELOCITY_MODE determineMode(int velocity, int acceleration) {
        if(velocity != 0 && acceleration != 0) {
            return VELOCITY_MODE.TRAPEZOID;
        } else if(velocity != 0) {
            return VELOCITY_MODE.RECTANGLE;
        } else {
            return VELOCITY_MODE.STEP;
        }
    }

    @Override
    public String toString() {
        return "VelocityModeCommand{" +
                "servoId='" + servoId + '\'' +
                ", mode=" + mode +
                '}';
    }
}
