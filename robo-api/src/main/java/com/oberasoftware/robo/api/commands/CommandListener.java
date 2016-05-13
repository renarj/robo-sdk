package com.oberasoftware.robo.api.commands;

/**
 * @author Renze de Vries
 */
public interface CommandListener<T extends RobotCommand> {
    void receive(T command);
}
