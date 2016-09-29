package com.oberasoftware.robo.api.motion.controller;

/**
 * @author Renze de Vries
 */
public interface HandsController extends MotionController {
    @Override
    default String getName() {
        return "hands";
    }

    void openHands();

    void closeHands();
}
