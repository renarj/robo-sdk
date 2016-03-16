package com.oberasoftware.robo.api.events;

import com.oberasoftware.base.event.Event;

/**
 * @author Renze de Vries
 */
public class DistanceSensorEvent implements Event {
    private final String source;
    private final int distance;

    public DistanceSensorEvent(String source, int distance) {
        this.source = source;
        this.distance = distance;
    }

    public String getSource() {
        return source;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "DistanceSensorEvent{" +
                "source='" + source + '\'' +
                ", distance=" + distance +
                '}';
    }
}
