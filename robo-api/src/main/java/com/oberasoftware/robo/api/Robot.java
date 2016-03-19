package com.oberasoftware.robo.api;

import com.oberasoftware.base.event.EventHandler;

/**
 * @author Renze de Vries
 */
public interface Robot {
    void listen(EventHandler robotEventHandler);

    MotionEngine getMotionEngine();

    ServoDriver getServoDriver();

    void shutdown();
}
