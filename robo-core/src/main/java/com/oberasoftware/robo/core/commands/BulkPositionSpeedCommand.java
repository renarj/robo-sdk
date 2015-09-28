package com.oberasoftware.robo.core.commands;

import com.oberasoftware.base.event.Event;

import java.util.Map;

/**
 * @author Renze de Vries
 */
public class BulkPositionSpeedCommand implements Event {
    private Map<String, PositionAndSpeedCommand> commands;

    public BulkPositionSpeedCommand(Map<String, PositionAndSpeedCommand> commands) {
        this.commands = commands;
    }

    public Map<String, PositionAndSpeedCommand> getCommands() {
        return commands;
    }

    @Override
    public String toString() {
        return "BulkPositionSpeedCommand{" +
                "commands=" + commands +
                '}';
    }
}

