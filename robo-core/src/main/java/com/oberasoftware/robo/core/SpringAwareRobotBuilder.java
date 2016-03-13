package com.oberasoftware.robo.core;

import com.oberasoftware.robo.api.MotionEngine;
import com.oberasoftware.robo.api.MotionResource;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.ServoDriver;
import com.oberasoftware.robo.api.sensors.Sensor;
import org.springframework.context.ApplicationContext;

/**
 * @author Renze de Vries
 */
public class SpringAwareRobotBuilder {
    private final ApplicationContext context;

    private MotionEngine motionEngine;
    private ServoDriver servoDriver;

    public SpringAwareRobotBuilder(ApplicationContext context) {
        this.context = context;
    }

    public SpringAwareRobotBuilder motionEngine(MotionEngine motionEngine) {
        this.motionEngine = motionEngine;
        return this;
    }

    public SpringAwareRobotBuilder motionEngine(Class<? extends MotionEngine> motionEngineClass, MotionResource resource, Object... args) {
        this.motionEngine = context.getBean(motionEngineClass, args);
        this.motionEngine.loadResource(resource);

        return this;
    }


    public SpringAwareRobotBuilder servoDriver(ServoDriver servoDriver) {
        this.servoDriver = servoDriver;
        return this;
    }

    public SpringAwareRobotBuilder servoDriver(Class<? extends ServoDriver> servoDriverClass, Object... args) {
        this.servoDriver = context.getBean(servoDriverClass, args);
        return this;
    }

    public SpringAwareRobotBuilder sensor(Sensor sensor) {
        return this;
    }

    public Robot build() {
        return null;
    }
}
