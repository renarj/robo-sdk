package com.oberasoftware.robo.api.motion;

import java.util.List;
import java.util.Set;

/**
 * @author Renze de Vries
 */
public interface KeyFrame {
    String getMotionId();

    String getKeyFrameId();

    Set<String> getServoIds();

    List<ServoStep> getServoSteps();

    long getTimeInMs();
}
