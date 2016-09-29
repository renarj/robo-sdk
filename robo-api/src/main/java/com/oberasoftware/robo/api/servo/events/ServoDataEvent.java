package com.oberasoftware.robo.api.servo.events;

import com.oberasoftware.base.event.Event;
import com.oberasoftware.robo.api.servo.ServoData;

/**
 * @author Renze de Vries
 */
public interface ServoDataEvent extends Event {
    String getServoId();

    ServoData getServoData();
}
