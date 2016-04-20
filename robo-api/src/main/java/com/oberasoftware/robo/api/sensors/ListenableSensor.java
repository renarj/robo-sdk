package com.oberasoftware.robo.api.sensors;

/**
 * @author Renze de Vries
 */
public interface ListenableSensor<T extends SensorValue> extends Sensor<T> {
    void listen(SensorListener<T> listener);
}
