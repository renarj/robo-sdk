package com.oberasoftware.robo.api;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.robo.api.servo.ServoDriver;

/**
 * @author Renze de Vries
 */
public interface Robot {
    String getName();

    void listen(EventHandler robotEventHandler);

    MotionEngine getMotionEngine();

    ServoDriver getServoDriver();

    void shutdown();
}
