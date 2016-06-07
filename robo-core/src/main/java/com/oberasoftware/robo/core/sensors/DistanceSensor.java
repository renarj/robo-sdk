package com.oberasoftware.robo.core.sensors;

import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.events.DistanceSensorEvent;
import com.oberasoftware.robo.api.sensors.AnalogPort;
import com.oberasoftware.robo.api.sensors.DirectPort;
import com.oberasoftware.robo.api.sensors.DistanceValue;
import com.oberasoftware.robo.api.sensors.Port;
import com.oberasoftware.robo.api.sensors.SensorDriver;
import com.oberasoftware.robo.api.sensors.SensorValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Renze de Vries
 */
public class DistanceSensor extends AbstractSensor<DistanceValue> {
    private static final Logger LOG = LoggerFactory.getLogger(DistanceSensor.class);

    private static final AnalogToDistanceConverter CONVERTER = new AnalogToDistanceConverter();

    private final String name;
    private final String portName;

    private Robot robot;
    private Port port;

    private AtomicInteger lastDistance = new AtomicInteger(0);

    public DistanceSensor(String name, String portName) {
        this.name = name;
        this.portName = portName;
    }

    private void notifyListeners(int distance) {
        LOG.debug("Received a Distance: {} on port: {}", distance, port);

        DistanceSensorEvent event = new DistanceSensorEvent(robot.getName(), portName, name, distance);
        notifyListeners(event);
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
    public void activate(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void activate(Robot robot, SensorDriver sensorDriver) {
        activate(robot);
        this.port = sensorDriver.getPort(portName);
        if(this.port instanceof AnalogPort) {
            ((AnalogPort)this.port).listen(e -> notifyListeners(CONVERTER.convert(e.getRaw())));
        } else {
            ((DirectPort<SensorValue<Integer>>)this.port).listen(e -> notifyListeners(e.getRaw()));
        }
    }
}
