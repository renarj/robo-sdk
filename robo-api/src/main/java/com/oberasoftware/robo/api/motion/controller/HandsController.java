package com.oberasoftware.robo.api.motion.controller;

/**
 * @author Renze de Vries
 */
public interface HandsController extends MotionController {

    String CONTROLLER_NAME = "hands";

    enum HAND_ID {
        LEFT,
        RIGHT,
        ALL
    }

    @Override
    default String getName() {
        return CONTROLLER_NAME;
    }

    void openHand(HAND_ID hand);

    void openHands();

    void closeHand(HAND_ID hand);

    void closeHands();
}
