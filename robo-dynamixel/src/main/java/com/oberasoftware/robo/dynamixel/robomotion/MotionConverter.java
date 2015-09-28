package com.oberasoftware.robo.dynamixel.robomotion;

import com.oberasoftware.robo.api.motion.Motion;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface MotionConverter {
    List<Motion> loadMotions(String motionFile);
}
