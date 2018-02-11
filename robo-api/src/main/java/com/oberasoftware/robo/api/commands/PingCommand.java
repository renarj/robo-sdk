package com.oberasoftware.robo.api.commands;

/**
 * @author renarj
 */
public class PingCommand implements Command {
    private final String name;

    public PingCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
