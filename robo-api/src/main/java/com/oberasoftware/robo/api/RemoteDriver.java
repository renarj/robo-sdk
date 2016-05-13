package com.oberasoftware.robo.api;

import com.oberasoftware.robo.api.commands.CommandListener;
import com.oberasoftware.robo.api.events.RobotEvent;

/**
 * @author Renze de Vries
 */
public interface RemoteDriver {
    void activate(Robot robot);

    void publish(RobotEvent robotEvent);

    void register(CommandListener<?> commandListener);
}
