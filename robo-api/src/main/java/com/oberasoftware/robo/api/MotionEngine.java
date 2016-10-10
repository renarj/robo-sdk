package com.oberasoftware.robo.api;

import com.oberasoftware.robo.api.motion.KeyFrame;
import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.api.motion.MotionResource;
import com.oberasoftware.robo.api.motion.WalkDirection;
import com.oberasoftware.robo.api.motion.controller.MotionController;

import java.util.List;
import java.util.Optional;

/**
 * @author Renze de Vries
 */
public interface MotionEngine extends ActivatableCapability {

    boolean prepareWalk();

    boolean rest();

    void loadResource(MotionResource resource);

    <T extends MotionController> Optional<T> getMotionController(String controllerName);

    List<String> getMotions();

    MotionTask walkForward();

    MotionTask walk(WalkDirection direction);

    MotionTask walk(WalkDirection direction, float meters);

    MotionTask runMotion(String motionName);

    MotionTask runMotion(Motion motion);

    MotionTask runMotion(KeyFrame keyFrame);

    MotionTask goToPosture(String posture);

    List<MotionTask> getActiveTasks();

    KeyFrame getCurrentPositionAsKeyFrame();

    boolean stopTask(MotionTask task);

    boolean stopAllTasks();

    boolean stopWalking();
}
