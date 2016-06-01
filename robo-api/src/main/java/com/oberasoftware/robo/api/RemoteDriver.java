package com.oberasoftware.robo.api;

import com.oberasoftware.base.event.Event;
import com.oberasoftware.robo.api.commands.CommandListener;

/**
 * @author Renze de Vries
 */
public interface RemoteDriver extends ActivatableCapability {
    void publish(Event robotEvent);

    void register(CommandListener<?> commandListener);
}
