package com.oberasoftware.robo.core.sensors;

import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.sensors.DirectPort;
import com.oberasoftware.robo.api.sensors.SensorDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Renze de Vries
 */
public class ServoSensor extends AbstractSensor<PositionValue> {
    private static final Logger LOG = LoggerFactory.getLogger(ServoSensor.class);

    private final String name;
    private final String portName;

    public ServoSensor(String name, String portName) {
        this.name = name;
        this.portName = portName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PositionValue getValue() {
        return null;
    }

    @Override
    public void activate(Robot robot) {
    }

    @Override
    public void activate(Robot robot, SensorDriver sensorDriver) {
        if(sensorDriver instanceof ServoSensorDriver) {
            LOG.debug("Activating servo port: {}", portName);
            DirectPort<PositionValue> port = ((ServoSensorDriver) sensorDriver).getPort(portName);
            if(port != null) {
                port.listen(e -> notifyListeners(new ServoPositionEvent(robot.getName(), name, e.getServoId(), e)));
            } else {
                LOG.warn("Could not activate servo senso, no port: {} was found", portName);
            }
        }
    }
}
