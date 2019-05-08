package com.oberasoftware.robo.api.commands;

import com.oberasoftware.base.event.Event;

import java.util.Map;

/**
 * @author Renze de Vries
 */
public class BulkPositionSpeedCommand implements Event {
    public enum WRITE_MODE {
        SYNC,
        REGISTERED_WRITE,
        DIRECT
    }

    private final Map<String, PositionAndSpeedCommand> commands;
    private final WRITE_MODE mode;

    public BulkPositionSpeedCommand(Map<String, PositionAndSpeedCommand> commands) {
        this(commands, WRITE_MODE.SYNC);
    }

    public BulkPositionSpeedCommand(Map<String, PositionAndSpeedCommand> commands, WRITE_MODE mode) {
        this.commands = commands;
        this.mode = mode;
    }

    public WRITE_MODE getMode() {
        return mode;
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

