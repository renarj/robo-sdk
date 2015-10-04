package com.oberasoftware.robo.api;

import com.oberasoftware.robo.api.motion.Motion;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface MotionConverter {
    List<Motion> loadMotions(String motionFile);
}
