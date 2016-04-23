package com.oberasoftware.robo.api;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface MotionEngine extends ActivatableCapability {

    boolean prepareWalk();

    void loadResource(MotionResource resource);

    MotionTask walk();

    MotionTask runMotion(String motionName);

    List<MotionTask> getActiveTasks();

    boolean stopTask(MotionTask task);

    boolean stopAllTasks();

    boolean stopWalking();
}
