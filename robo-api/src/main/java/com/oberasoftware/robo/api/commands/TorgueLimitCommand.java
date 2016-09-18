package com.oberasoftware.robo.api.commands;

import com.oberasoftware.robo.api.servo.ServoCommand;

/**
 * @author Renze de Vries
 */
public class TorgueLimitCommand implements ServoCommand {
    private final String servoId;
    private final int torgueLimit;

    public TorgueLimitCommand(String servoId, int torgueLimit) {
        this.servoId = servoId;
        this.torgueLimit = torgueLimit;
    }

    @Override
    public String getServoId() {
        return servoId;
    }

    public int getTorgueLimit() {
        return torgueLimit;
    }

    @Override
    public String toString() {
        return "TorgueLimitCommand{" +
                "servoId='" + servoId + '\'' +
                ", torgueLimit=" + torgueLimit +
                '}';
    }
}
