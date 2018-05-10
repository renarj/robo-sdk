package com.oberasoftware.robo.core.commands;

import com.oberasoftware.robo.api.servo.ServoCommand;

/**
 * @author renarj
 */
public class AngleLimitCommand implements ServoCommand {
    private String servoId;
    private int minLimit;
    private int maxLimit;

    public AngleLimitCommand(String servoId, int minLimit, int maxLimit) {
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
        return "AngleLimitCommand{" +
                "servoId='" + servoId + '\'' +
                ", minLimit=" + minLimit +
                ", maxLimit=" + maxLimit +
                '}';
    }
}
