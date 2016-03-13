package com.oberasoftware.robo.core;

import com.oberasoftware.base.event.EventBus;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.impl.LocalEventBus;
import com.oberasoftware.robo.api.MotionEngine;
import com.oberasoftware.robo.api.Robot;
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

    private EventBus eventBus = new LocalEventBus();

    private final Map<String, Sensor> sensors;
    private final MotionEngine motionEngine;

    public GenericRobot(MotionEngine motionEngine, List<Sensor> sensors) {
        this.sensors = sensors.stream().collect(Collectors.toMap(Sensor::getName, sensor -> sensor));
        this.motionEngine = motionEngine;
    }

    @Override
    public void listen(EventHandler robotEventHandler) {
        eventBus.registerHandler(robotEventHandler);
    }

    @Override
    public MotionEngine getMotionEngine() {
        return this.motionEngine;
    }
}
