package com.oberasoftware.robo.api.events;

import com.oberasoftware.robo.api.sensors.DistanceValue;

/**
 * @author Renze de Vries
 */
public class DistanceSensorEvent implements SensorEvent<DistanceValue> {
    private final String source;
    private final int distance;

    public DistanceSensorEvent(String source, int distance) {
        this.source = source;
        this.distance = distance;
    }

    @Override
    public String getSource() {
        return source;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public DistanceValue getValue() {
        return () -> distance;
    }

    @Override
    public String toString() {
        return "DistanceSensorEvent{" +
                "source='" + source + '\'' +
                ", distance=" + distance +
                '}';
    }
}
