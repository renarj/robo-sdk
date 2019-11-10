package com.oberasoftware.robo.api.motion;

import java.util.List;
import java.util.Optional;

/**
 * @author Renze de Vries
 */
public interface MotionManager {
    void storeMotion(Motion motion);

    Optional<Motion> findMotionByName(String motionName);

    List<Motion> findMotions();
}
