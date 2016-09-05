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

    List<String> getMotions();

    MotionTask walkForward();

    MotionTask walk(WalkDirection direction);

    MotionTask walk(WalkDirection direction, int meters);

    MotionTask runMotion(String motionName);

    MotionTask goToPosture(String posture);

    List<MotionTask> getActiveTasks();

    boolean stopTask(MotionTask task);

    boolean stopAllTasks();

    boolean stopWalking();
}
