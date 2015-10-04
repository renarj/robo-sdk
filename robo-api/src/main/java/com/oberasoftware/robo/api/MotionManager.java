package com.oberasoftware.robo.api;

import com.oberasoftware.robo.api.motion.Motion;

import java.util.List;
import java.util.Optional;

/**
 * @author Renze de Vries
 */
public interface MotionManager {
    void storeMotion(Motion motion);

    Optional<Motion> findMotion(String motionName);

    List<Motion> findMotions();
}
