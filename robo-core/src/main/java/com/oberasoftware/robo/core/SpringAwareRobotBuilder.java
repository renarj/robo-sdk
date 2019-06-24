package com.oberasoftware.robo.core;

import com.oberasoftware.base.event.EventBus;
import com.oberasoftware.base.event.impl.LocalEventBus;
import com.oberasoftware.robo.api.*;
import com.oberasoftware.robo.api.motion.MotionResource;
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

/**
 * @author Renze de Vries
 */
public class SpringAwareRobotBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(SpringAwareRobotBuilder.class);

    private final String robotName;

    private final ApplicationContext context;
    private final EventBus eventBus;

    private List<SensorHolder> sensors = new ArrayList<>();
    private List<CapabilityHolder> capabilities = new ArrayList<>();
    private boolean isRemote = false;

    public SpringAwareRobotBuilder(String robotName, ApplicationContext context) {
        this.context = context;
        this.robotName = robotName;
        this.eventBus = context.getBean(LocalEventBus.class);
    }

    public SpringAwareRobotBuilder motionEngine(MotionEngine motionEngine, MotionResource resource) {
        if(resource != null) {
            motionEngine.loadResource(resource);
        }
        return addCapability(motionEngine, new HashMap<>());
    }

    public SpringAwareRobotBuilder motionEngine(Class<? extends MotionEngine> motionEngineClass, MotionResource resource) {
        return motionEngine(context.getBean(motionEngineClass), resource);
    }

    public SpringAwareRobotBuilder motionEngine(Class<? extends MotionEngine> motionEngineClass) {
        return motionEngine(context.getBean(motionEngineClass), null);
    }

    public SpringAwareRobotBuilder servoDriver(ServoDriver servoDriver, Map<String, String> properties) {
        return addCapability(servoDriver, properties);
    }

    public SpringAwareRobotBuilder servoDriver(Class<? extends ServoDriver> servoDriver) {
        return servoDriver(context.getBean(servoDriver), new HashMap<>());
    }

    public SpringAwareRobotBuilder servoDriver(Class<? extends ServoDriver> servoDriverClass, Map<String, String> properties) {
        return servoDriver(context.getBean(servoDriverClass), properties);
    }

    public SpringAwareRobotBuilder remote(Class<? extends RemoteDriver> remoteConnector, boolean isRemote) {
        RemoteDriver remoteDriver = context.getBean(remoteConnector);
        Map<String, String> properties = new HashMap<>();
        this.isRemote = isRemote;
        return addCapability(remoteDriver, properties);
    }

    public SpringAwareRobotBuilder remote(Class<? extends RemoteDriver> remoteConnector) {
        return remote(remoteConnector, false);
    }

    public SpringAwareRobotBuilder capability(Class<? extends Capability> capabilityClass) {
        return capability(capabilityClass, new HashMap<>());
    }

    public SpringAwareRobotBuilder capability(Class<? extends Capability> capabilityClass, Map<String, String> properties) {
        Capability capability = context.getBean(capabilityClass);
        return addCapability(capability, properties);
    }

    private SpringAwareRobotBuilder addCapability(Capability capability, Map<String, String> properties) {
        if(!capabilities.stream().filter(c -> c.getCapability().equals(capability)).findFirst().isPresent()) {
            capabilities.add(new CapabilityHolder(capability, properties));
        }

        return this;
    }

    public SpringAwareRobotBuilder sensor(Sensor sensor, Class<? extends SensorDriver> sensorDriverClass) {
        SensorDriver driver = null;
        if(sensorDriverClass != null) {
            driver = context.getBean(sensorDriverClass);
            addCapability(driver, new HashMap<>());
        }

        this.sensors.add(new SensorHolder(sensor, driver, eventBus));
        return this;
    }

    public Robot build() {
        Robot robot = buildRobot();
        context.getBean(RobotRegistry.class).register(robot);

        return robot;
    }

    private Robot buildRobot() {
        LOG.info("Creating robot base system");
        GenericRobot robot = new GenericRobot(robotName, isRemote, eventBus, capabilities, sensors);
        robot.initialize();

        RemoteDriver remoteDriver = robot.getRemoteDriver();
        if(remoteDriver != null) {
            LOG.info("Remote robot control is enabled");
            return new RemoteEnabledRobot(remoteDriver, robot, isRemote);
        } else {
            LOG.info("Robot construction finished");
            return robot;
        }
    }

}
