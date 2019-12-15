package com.oberasoftware.robo.core.sensors;

import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.sensors.*;

/**
 * @author Renze de Vries
 */
public class GyroSensor implements SingeValueSensor<PercentageValue> {

    private final String name;
    private final AnalogPort xAxis;
    private final AnalogPort yAxis;
    private final SensorConverter converter;

    public GyroSensor(String name, AnalogPort xAxis, AnalogPort yAxis, SensorConverter converter) {
        this.name = name;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.converter = converter;
    }

    @Override
    public PercentageValue getValue() {
        return null;
    }

    @Override
    public void activate(Robot robot) {

    }

    @Override
    public String getName() {
        return name;
    }
}
