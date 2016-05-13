package com.oberasoftware.robo.core;

import com.oberasoftware.base.event.EventBus;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.robo.api.MotionEngine;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.servo.ServoDriver;
import com.oberasoftware.robo.api.sensors.Sensor;
import com.oberasoftware.robo.api.sensors.SensorDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private Map<String, Sensor> sensors;

    public GenericRobot(String robotName, EventBus eventBus, MotionEngine motionEngine,
                        ServoDriver servoDriver, List<SensorDriver> sensorDrivers) {
        this.robotName = robotName;
        this.eventBus = eventBus;
        this.servoDriver = servoDriver;
        this.motionEngine = motionEngine;
        this.sensorDrivers = sensorDrivers;
    }

    @Override
    public String getName() {
        return robotName;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors.stream().collect(Collectors.toMap(Sensor::getName, sensor -> sensor));
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
    public void shutdown() {
        motionEngine.rest();
        sensorDrivers.forEach(SensorDriver::close);
        motionEngine.stopAllTasks();
        servoDriver.shutdown();
    }
}
