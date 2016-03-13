package com.oberasoftware.robo.api;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface MotionEngine {
    MotionTask walk();

    List<MotionTask> getActiveTasks();

    boolean stopWalking();
}
