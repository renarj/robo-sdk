package com.oberasoftware.robo.api.motion;

import com.oberasoftware.robo.api.MotionTask;

/**
 * @author Renze de Vries
 */
public interface MotionExecutor {
    MotionTask execute(Motion motion);

    MotionTask execute(KeyFrame keyFrame);
}
