package com.oberasoftware.robo.core.sensors;

import com.oberasoftware.robo.api.events.DistanceSensorEvent;
import com.oberasoftware.robo.api.sensors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Renze de Vries
 */
public class DistanceSensor implements ListenableSensor<DistanceValue> {
    private static final Logger LOG = LoggerFactory.getLogger(DistanceSensor.class);

    private final String name;
    private final Port port;

    private List<SensorListener<DistanceValue>> sensorListeners = new CopyOnWriteArrayList<>();

    private AtomicInteger lastDistance = new AtomicInteger(0);

    public DistanceSensor(String name, AnalogPort port, SensorConverter<Double, Integer> converter) {
        this.name = name;
        this.port = port;

        port.listen(e -> notifyListeners(converter.convert(e.getRaw())));
    }

    public DistanceSensor(String name, DirectPort<SensorValue<Integer>> directPort) {
        this.name = name;
        this.port = directPort;

        directPort.listen(e -> notifyListeners(e.getRaw()));
    }

    private void notifyListeners(int distance) {
        LOG.debug("Received a Distance: {} on port: {}", distance, port);

        DistanceSensorEvent event = new DistanceSensorEvent(name, distance);
        sensorListeners.forEach(l -> l.receive(event));
    }

    @Override
    public DistanceValue getValue() {
        return () -> lastDistance.get();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void listen(SensorListener<DistanceValue> listener) {
        this.sensorListeners.add(listener);
    }
}
