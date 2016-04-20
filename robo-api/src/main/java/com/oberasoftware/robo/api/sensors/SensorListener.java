package com.oberasoftware.robo.api.sensors;

import com.oberasoftware.robo.api.events.SensorEvent;

/**
 * @author Renze de Vries
 */
public interface SensorListener<T extends SensorValue> {
    void receive(SensorEvent<T> event);
}
