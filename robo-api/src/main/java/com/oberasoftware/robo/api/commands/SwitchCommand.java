package com.oberasoftware.robo.api.commands;

/**
 * @author renarj
 */
public interface SwitchCommand extends ItemValueCommand {
    enum STATE {
        ON,
        OFF
    }

    STATE getState();
}
