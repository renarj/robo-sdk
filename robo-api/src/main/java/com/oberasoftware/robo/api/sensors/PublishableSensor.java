package com.oberasoftware.robo.api.sensors;

import com.oberasoftware.base.event.EventBus;

/**
 * @author Renze de Vries
 */
public interface PublishableSensor<T extends SensorValue> extends Sensor<T> {

    void activate(EventBus bus);
}
