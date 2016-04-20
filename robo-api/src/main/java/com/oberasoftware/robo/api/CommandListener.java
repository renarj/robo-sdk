package com.oberasoftware.robo.api;

import com.oberasoftware.robo.api.commands.RobotCommand;

/**
 * @author Renze de Vries
 */
public interface CommandListener<T extends RobotCommand> {
    void receive(T command);
}
