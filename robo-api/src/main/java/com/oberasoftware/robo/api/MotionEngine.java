package com.oberasoftware.robo.api;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface MotionEngine {

    boolean prepareWalk();

    void loadResource(MotionResource resource);

    MotionTask walk();

    List<MotionTask> getActiveTasks();

    boolean stopWalking();
}
