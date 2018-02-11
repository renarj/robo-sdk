package com.oberasoftware.robo.dynamixel.commands;

import com.oberasoftware.robo.api.servo.ServoCommand;

/**
 * @author renarj
 */
public class DynamixelAngleLimitCommand implements ServoCommand {
    public enum MODE {
        JOINT_MODE,
        WHEEL_MODE
    }

    private String servoId;
    private int minLimit;
    private int maxLimit;

    public DynamixelAngleLimitCommand(String servoId, MODE servoMode) {
        this.servoId = servoId;
        if (servoMode == MODE.WHEEL_MODE) {
            minLimit = 0;
            maxLimit = 0;
        } else {
            minLimit = 0;
            maxLimit = 1023;
        }
    }

    public DynamixelAngleLimitCommand(String servoId, int minLimit, int maxLimit) {
        this.servoId = servoId;
        this.minLimit = minLimit;
        this.maxLimit = maxLimit;
    }

    @Override
    public String getServoId() {
        return servoId;
    }

    public int getMinLimit() {
        return minLimit;
    }

    public int getMaxLimit() {
        return maxLimit;
    }

    @Override
    public String toString() {
        return "DynamixelAngleLimitCommand{" +
                "servoId='" + servoId + '\'' +
                ", minLimit=" + minLimit +
                ", maxLimit=" + maxLimit +
                '}';
    }
}
