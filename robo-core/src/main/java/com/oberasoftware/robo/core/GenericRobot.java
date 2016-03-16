package com.oberasoftware.robo.core;

import com.oberasoftware.base.event.EventBus;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.robo.api.MotionEngine;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.ServoDriver;
import com.oberasoftware.robo.api.sensors.Sensor;
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

    private final Map<String, Sensor> sensors;
    private final MotionEngine motionEngine;
    private final ServoDriver servoDriver;

    public GenericRobot(EventBus eventBus, MotionEngine motionEngine, ServoDriver servoDriver, List<Sensor> sensors) {
        this.eventBus = eventBus;
        this.sensors = sensors.stream().collect(Collectors.toMap(Sensor::getName, sensor -> sensor));
        this.servoDriver = servoDriver;
        this.motionEngine = motionEngine;
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
}
