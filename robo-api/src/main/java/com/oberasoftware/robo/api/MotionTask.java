package com.oberasoftware.robo.api;

import com.oberasoftware.robo.api.motion.Motion;

/**
 * @author Renze de Vries
 */
public interface MotionTask {

    String getTaskId();

    Motion getMotion();

    boolean isCancelled();

    boolean isRunning();

    void cancel();

    void start();

    void awaitCompletion();
}
