package com.oberasoftware.robo.core;

import com.oberasoftware.base.event.EventBus;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.robo.api.ActivatableCapability;
import com.oberasoftware.robo.api.Capability;
import com.oberasoftware.robo.api.MotionEngine;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.servo.ServoDriver;
import com.oberasoftware.robo.api.sensors.Sensor;
import com.oberasoftware.robo.api.sensors.SensorDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Renze de Vries
 */
public class GenericRobot implements Robot {
    private static final Logger LOG = LoggerFactory.getLogger(GenericRobot.class);

    private EventBus eventBus;

    private final List<SensorDriver> sensorDrivers;
    private final MotionEngine motionEngine;
    private final ServoDriver servoDriver;
    private final String robotName;

    private final List<Capability> capabilities = new ArrayList<>();

    private Map<String, Sensor> sensors;

    public GenericRobot(String robotName, EventBus eventBus, MotionEngine motionEngine,
                        ServoDriver servoDriver, List<SensorDriver> sensorDrivers, List<Capability> otherCapabilities) {
        this.robotName = robotName;
        this.eventBus = eventBus;
        this.servoDriver = servoDriver;
        this.motionEngine = motionEngine;
        this.sensorDrivers = sensorDrivers;
        this.capabilities.add(motionEngine);
        this.capabilities.add(servoDriver);
        otherCapabilities.forEach(capabilities::add);
    }

    @Override
    public String getName() {
        return robotName;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors.stream().collect(Collectors.toMap(Sensor::getName, sensor -> sensor));
        sensors.forEach(capabilities::add);
    }

    @Override
    public void listen(EventHandler robotEventHandler) {
        eventBus.registerHandler(robotEventHandler);
    }

    @Override
    public ServoDriver getServoDriver() {
        return this.servoDriver;
    }

    @Override
    public MotionEngine getMotionEngine() {
        return this.motionEngine;
    }

    @Override
    public List<Capability> getCapabilities() {
        return capabilities;
    }

    @Override
    public void shutdown() {
        LOG.info("Shutting down robot: {}", robotName);
        motionEngine.rest();
        sensorDrivers.forEach(SensorDriver::close);
        motionEngine.stopAllTasks();
        servoDriver.shutdown();

        capabilities.forEach(c -> {
            if(c instanceof ActivatableCapability) {
                ((ActivatableCapability)c).shutdown();
            }
        });
        LOG.info("Robot: {} shutdown complete", robotName);
    }
}
