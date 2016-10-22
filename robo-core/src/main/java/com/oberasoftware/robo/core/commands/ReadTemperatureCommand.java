package com.oberasoftware.robo.core.commands;

import com.oberasoftware.robo.api.servo.ServoCommand;

/**
 * @author Renze de Vries
 */
public class ReadTemperatureCommand implements ServoCommand {
    private final String servoId;

    public ReadTemperatureCommand(String servoId) {
        this.servoId = servoId;
    }

    @Override
    public String getServoId() {
        return servoId;
    }

    @Override
    public String toString() {
        return "ReadTemperatureCommand{" +
                "servoId='" + servoId + '\'' +
                '}';
    }
}
