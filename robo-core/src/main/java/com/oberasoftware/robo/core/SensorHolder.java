package com.oberasoftware.robo.core;

import com.oberasoftware.base.event.EventBus;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.sensors.ListenableSensor;
import com.oberasoftware.robo.api.sensors.Sensor;
import com.oberasoftware.robo.api.sensors.SensorDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Renze de Vries
 */
public class SensorHolder {
    private static final Logger LOG = LoggerFactory.getLogger(SensorHolder.class);

    private final Sensor sensor;
    private final SensorDriver driver;
    private final EventBus eventBus;

    public SensorHolder(Sensor sensor, SensorDriver driver, EventBus eventBus) {
        this.sensor = sensor;
        this.driver = driver;
        this.eventBus = eventBus;
    }

    public void initializeSensor(Robot robot) {
        LOG.info("Activating sensor: {}", sensor);
        sensor.activate(robot, driver);

        if (sensor instanceof ListenableSensor) {
            LOG.info("Activating publishable sensor: {}", sensor);
            ((ListenableSensor) sensor).listen(event -> eventBus.publish(event));
        }
    }
}
