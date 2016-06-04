package com.oberasoftware.robo.api.events;

import com.oberasoftware.base.event.Event;

/**
 * @author Renze de Vries
 */
public interface RobotEvent extends Event {
    String getControllerId();

    String getItemId();

    String getLabel();
}
