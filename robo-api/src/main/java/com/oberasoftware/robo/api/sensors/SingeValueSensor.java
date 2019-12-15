package com.oberasoftware.robo.api.sensors;

public interface SingeValueSensor<T extends SensorValue> extends Sensor {
    String getName();

    T getValue();
}
