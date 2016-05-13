package com.oberasoftware.robo.api.servo;

import com.oberasoftware.base.event.Event;

/**
 * @author Renze de Vries
 */
public interface ServoUpdateEvent extends Event {
    String getServoId();

    ServoData getServoData();
}
