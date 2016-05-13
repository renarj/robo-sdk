package com.oberasoftware.robo.api;

import com.oberasoftware.robo.api.motion.MotionResource;
import com.oberasoftware.robo.api.motion.WalkDirection;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface MotionEngine extends ActivatableCapability {

    boolean prepareWalk();

    boolean rest();

    void loadResource(MotionResource resource);

    MotionTask walkForward();

    MotionTask walk(WalkDirection direction);

    MotionTask runMotion(String motionName);

    List<MotionTask> getActiveTasks();

    boolean stopTask(MotionTask task);

    boolean stopAllTasks();

    boolean stopWalking();
}
