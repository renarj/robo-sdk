package com.oberasoftware.robo.api.motion;

/**
 * @author Renze de Vries
 */
public interface MotionExecutor {
    void execute(Motion motion);

    void execute(Motion motion, int repeats);
}
