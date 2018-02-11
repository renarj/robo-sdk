package com.oberasoftware.robo.api.events;

import com.oberasoftware.base.event.Event;
import com.oberasoftware.robo.api.model.Value;

/**
 * @author Renze de Vries
 */
public interface ValueEvent extends Event {
    String getLabel();

    Value getValue();
}
