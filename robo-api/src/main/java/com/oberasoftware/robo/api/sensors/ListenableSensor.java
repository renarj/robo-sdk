package com.oberasoftware.robo.api.sensors;

/**
 * @author Renze de Vries
 */
public interface ListenableSensor<T extends SensorValue> extends SingeValueSensor {
    void listen(SensorListener<T> listener);
}
