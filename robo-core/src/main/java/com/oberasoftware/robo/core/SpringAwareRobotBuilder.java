package com.oberasoftware.robo.core;

import com.oberasoftware.base.event.EventBus;
import com.oberasoftware.base.event.impl.LocalEventBus;
import com.oberasoftware.robo.api.*;
import com.oberasoftware.robo.api.motion.MotionResource;
import com.oberasoftware.robo.api.sensors.ListenableSensor;
import com.oberasoftware.robo.api.sensors.Sensor;
import com.oberasoftware.robo.api.sensors.SensorDriver;
import com.oberasoftware.robo.api.servo.ServoDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Renze de Vries
 */
public class SpringAwareRobotBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(SpringAwareRobotBuilder.class);

    private final ApplicationContext context;

    private final String robotName;
    private final EventBus eventBus = new LocalEventBus();

    private MotionEngine motionEngine;
    private ServoDriver servoDriver;
    private List<SensorHolder> sensors = new ArrayList<>();
    private RemoteDriver remoteDriver = null;
    private List<SensorDriver> sensorDrivers = new ArrayList<>();
    private List<Capability> otherCapabilities = new ArrayList<>();

    public SpringAwareRobotBuilder(String robotName, ApplicationContext context) {
        this.context = context;
        this.robotName = robotName;
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

    public SpringAwareRobotBuilder capability(Class<? extends Capability> capabilityClass) {
        return capability(capabilityClass, new HashMap<>());
    }

    public SpringAwareRobotBuilder capability(Class<? extends Capability> capabilityClass, Map<String, String> properties) {
        Capability capability = context.getBean(capabilityClass);
        otherCapabilities.add(capability);
        if(capability instanceof ActivatableCapability) {
            ((ActivatableCapability)capability).activate(properties);
        }

        return this;
    }

    public SpringAwareRobotBuilder sensor(Sensor sensor, Class<? extends SensorDriver> sensorDriverClass) {
        SensorDriver driver = null;
        if(sensorDriverClass != null) {
            driver = context.getBean(sensorDriverClass);
            sensorDrivers.add(driver);
        }

        this.sensors.add(new SensorHolder(sensor, driver));
        return this;
    }

    public Robot build() {
        LOG.info("Initializing all sensor drivers");
        initializeDrivers();

        LOG.info("Creating robot base system");
        GenericRobot robot = new GenericRobot(robotName, eventBus, motionEngine,
                servoDriver, sensorDrivers, otherCapabilities);

        LOG.info("Activating all sensors");
        sensors.forEach(s -> s.initializeSensor(robot));
        robot.setSensors(sensors.stream().map(h -> h.sensor).collect(Collectors.toList()));

        if(remoteDriver != null) {
            LOG.info("Remote robot control is enabled");
            remoteDriver.activate(robot);
            return new RemoteEnabledRobot(remoteDriver, robot);
        } else {
            LOG.info("Robot construction finished");
            return robot;
        }
    }

    private void initializeDrivers() {
        sensorDrivers.forEach(SensorDriver::initialize);
    }

    private class SensorHolder {
        private final Sensor sensor;
        private final SensorDriver driver;

        public SensorHolder(Sensor sensor, SensorDriver driver) {
            this.sensor = sensor;
            this.driver = driver;
        }

        private void initializeSensor(Robot robot) {
            LOG.info("Activating sensor: {}", sensor);
            sensor.activate(robot, driver);

            if(sensor instanceof ListenableSensor) {
                LOG.info("Activating publishable sensor: {}", sensor);
                ((ListenableSensor)sensor).listen(event -> eventBus.publish(event));
            }
        }
    }
}
