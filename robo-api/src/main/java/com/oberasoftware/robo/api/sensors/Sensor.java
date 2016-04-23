package com.oberasoftware.robo.api.sensors;

import com.oberasoftware.robo.api.Capability;

/**
 * @author Renze de Vries
 */
public interface Sensor<T extends SensorValue> extends Capability {
    String getName();

    T getValue();

    default void activate(SensorDriver sensorDriver) {
        //no activiation needed by default
    }
}
