package com.oberasoftware.robo.core;

import com.oberasoftware.base.event.EventBus;
import com.oberasoftware.robo.api.MotionEngine;
import com.oberasoftware.robo.api.MotionResource;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.ServoDriver;
import com.oberasoftware.robo.api.sensors.PublishableSensor;
import com.oberasoftware.robo.api.sensors.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Renze de Vries
 */
public class SpringAwareRobotBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(SpringAwareRobotBuilder.class);

    private final ApplicationContext context;

    private MotionEngine motionEngine;
    private ServoDriver servoDriver;
    private List<Sensor> sensors = new ArrayList<>();
    private EventBus eventBus;

    public SpringAwareRobotBuilder(ApplicationContext context) {
        this.context = context;
        this.eventBus = context.getBean(EventBus.class);
    }

    public SpringAwareRobotBuilder motionEngine(MotionEngine motionEngine, MotionResource resource) {
        this.motionEngine = motionEngine;
        this.motionEngine.loadResource(resource);

        return this;
    }

    public SpringAwareRobotBuilder motionEngine(Class<? extends MotionEngine> motionEngineClass, MotionResource resource, Object... args) {
        return motionEngine(context.getBean(motionEngineClass, args), resource);
    }

    public SpringAwareRobotBuilder servoDriver(ServoDriver servoDriver) {
        this.servoDriver = servoDriver;
        this.servoDriver.activate();
        return this;
    }

    public SpringAwareRobotBuilder servoDriver(Class<? extends ServoDriver> servoDriverClass, Object... args) {
        return servoDriver(context.getBean(servoDriverClass, args));
    }

    public SpringAwareRobotBuilder sensor(Sensor sensor) {
        if(sensor instanceof PublishableSensor) {
            LOG.info("Activating publishable sensor: {}", sensor);
            ((PublishableSensor)sensor).activate(eventBus);
        }
        this.sensors.add(sensor);
        return this;
    }

    public Robot build() {
        return new GenericRobot(eventBus, motionEngine, servoDriver, sensors);
    }
}
