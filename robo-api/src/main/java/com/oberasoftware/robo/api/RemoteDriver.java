package com.oberasoftware.robo.api;

import com.oberasoftware.robo.api.events.RobotEvent;

/**
 * @author Renze de Vries
 */
public interface RemoteDriver {
    void publish(RobotEvent robotEvent);

    void register(CommandListener<?> commandListener);
}
