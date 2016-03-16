package com.oberasoftware.robo.core.sensors;

import com.oberasoftware.base.event.EventBus;
import com.oberasoftware.robo.api.events.DistanceSensorEvent;
import com.oberasoftware.robo.api.sensors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Renze de Vries
 */
public class DistanceSensor implements PublishableSensor<DistanceValue> {
    private static final Logger LOG = LoggerFactory.getLogger(DistanceSensor.class);

    private final String name;
    private final AnalogPort port;

    private EventBus eventBus;

    public DistanceSensor(String name, AnalogPort port, SensorConverter<Double, Integer> converter) {
        this.name = name;
        this.port = port;

        this.port.listen(e -> {
            Double voltage = e.getRaw();
            int distance = converter.convert(voltage);

            LOG.debug("Received a voltage: {} on port: {} distance is: {}", voltage, port, distance);
            this.eventBus.publish(new DistanceSensorEvent(name, distance));
        });
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
