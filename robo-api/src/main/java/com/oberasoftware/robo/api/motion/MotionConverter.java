package com.oberasoftware.robo.api.motion;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface MotionConverter {
    List<Motion> loadMotions(String motionFile);
}
