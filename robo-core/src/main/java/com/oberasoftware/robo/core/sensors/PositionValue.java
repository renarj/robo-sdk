package com.oberasoftware.robo.core.sensors;

import com.oberasoftware.robo.api.sensors.SensorValue;

/**
 * @author Renze de Vries
 */
public class PositionValue implements SensorValue<Integer> {

    private final String servoId;
    private final int position;

    public PositionValue(String servoId, int position) {
        this.servoId = servoId;
        this.position = position;
    }

    @Override
    public Integer getRaw() {
        return position;
    }

    public String getServoId() {
        return servoId;
    }

    @Override
    public String toString() {
        return "PositionValue{" +
                "servoId='" + servoId + '\'' +
                ", position=" + position +
                '}';
    }
}
