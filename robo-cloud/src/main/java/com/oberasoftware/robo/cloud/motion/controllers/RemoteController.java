package com.oberasoftware.robo.cloud.motion.controllers;

import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.motion.controller.MotionController;

/**
 * @author Renze de Vries
 */
public interface RemoteController extends MotionController {
    void activate(Robot robot);
}
