package com.oberasoftware.robo.core;

import com.oberasoftware.base.event.EventBus;
import com.oberasoftware.base.event.impl.LocalEventBus;
import com.oberasoftware.robo.api.*;
import com.oberasoftware.robo.api.sensors.ListenableSensor;
import com.oberasoftware.robo.api.sensors.Sensor;
import com.oberasoftware.robo.api.sensors.SensorDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Renze de Vries
 */
public class SpringAwareRobotBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(SpringAwareRobotBuilder.class);

    private final ApplicationContext context;

    private MotionEngine motionEngine;
    private ServoDriver servoDriver;
    private List<Sensor> sensors = new ArrayList<>();
    private final EventBus eventBus = new LocalEventBus();
    private RemoteDriver remoteDriver = null;
    private List<SensorDriver> sensorDrivers = new ArrayList<>();

    public SpringAwareRobotBuilder(ApplicationContext context) {
        this.context = context;
    }

    public SpringAwareRobotBuilder motionEngine(MotionEngine motionEngine, MotionResource resource) {
        this.motionEngine = motionEngine;
        if(resource != null) {
            this.motionEngine.loadResource(resource);
        }
        this.motionEngine.activate(new HashMap<>());

        return this;
    }

    public SpringAwareRobotBuilder motionEngine(Class<? extends MotionEngine> motionEngineClass, MotionResource resource) {
        return motionEngine(context.getBean(motionEngineClass), resource);
    }

    public SpringAwareRobotBuilder motionEngine(Class<? extends MotionEngine> motionEngineClass) {
        return motionEngine(context.getBean(motionEngineClass), null);
    }

    public SpringAwareRobotBuilder servoDriver(ServoDriver servoDriver, Map<String, String> properties) {
        this.servoDriver = servoDriver;
        this.servoDriver.activate(properties);
        return this;
    }

    public SpringAwareRobotBuilder servoDriver(Class<? extends ServoDriver> servoDriver) {
        return servoDriver(context.getBean(servoDriver), new HashMap<>());
    }

    public SpringAwareRobotBuilder servoDriver(Class<? extends ServoDriver> servoDriverClass, Map<String, String> properties) {
        return servoDriver(context.getBean(servoDriverClass), properties);
    }

    public SpringAwareRobotBuilder remote(Class<? extends RemoteDriver> remoteConnector) {
        this.remoteDriver = context.getBean(remoteConnector);
        return this;
    }

    public SpringAwareRobotBuilder sensor(Sensor sensor, Class<? extends SensorDriver> sensorDriverClass) {
        if(sensorDriverClass != null) {
            SensorDriver sensorDriver = context.getBean(sensorDriverClass);
            sensorDriver.initialize();
            sensorDrivers.add(sensorDriver);

            LOG.info("Sensor requires a driver, activating and configuring driver");
            sensor.activate(sensorDriver);
        }
        if(sensor instanceof ListenableSensor) {
            LOG.info("Activating publishable sensor: {}", sensor);
            ((ListenableSensor)sensor).listen(event -> eventBus.publish(event));
        }
        this.sensors.add(sensor);
        return this;
    }

    public Robot build() {
        Robot robot = new GenericRobot(eventBus, motionEngine, servoDriver, sensorDrivers, sensors);
        if(remoteDriver != null) {
            LOG.info("Remote robot control is enabled");
            return new RemoteEnabledRobot(remoteDriver, robot);
        } else {
            return robot;
        }
    }
}
