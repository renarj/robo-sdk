package com.oberasoftware.robo.core.sensors;

import com.oberasoftware.base.event.EventBus;
import com.oberasoftware.robo.api.sensors.*;

/**
 * @author Renze de Vries
 */
public class DistanceSensor implements PublishableSensor<DistanceValue> {
    private final String name;
    private final AnalogPort port;
    private final SensorConverter converter;

    private EventBus eventBus;

    public DistanceSensor(String name, AnalogPort port, SensorConverter converter) {
        this.name = name;
        this.port = port;
        this.converter = converter;
    }

    @Override
    public DistanceValue getValue() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void activate(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}
