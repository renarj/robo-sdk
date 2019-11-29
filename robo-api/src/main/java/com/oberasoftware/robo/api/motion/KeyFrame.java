package com.oberasoftware.robo.api.motion;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface KeyFrame extends MotionUnit {
    String getKeyFrameId();

    List<JointTarget> getJointTargets();

    long getTimeInMs();
}
