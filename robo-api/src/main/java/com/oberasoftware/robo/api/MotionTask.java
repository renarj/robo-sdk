package com.oberasoftware.robo.api;

import com.oberasoftware.robo.api.motion.Motion;

/**
 * @author Renze de Vries
 */
public interface MotionTask {

    Motion getMotion();

    boolean isStopped();

    boolean isRunning();

    void cancel();

    void start();
}
