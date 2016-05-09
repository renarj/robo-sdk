package com.oberasoftware.robo.api.sensors;

import com.oberasoftware.robo.api.Capability;
import com.oberasoftware.robo.api.Robot;

/**
 * @author Renze de Vries
 */
public interface Sensor<T extends SensorValue> extends Capability {
    String getName();

    T getValue();

    default void activate(Robot robot, SensorDriver sensorDriver) {
        //no activiation needed by default
        activate(robot);
    }

    void activate(Robot robot);
}
