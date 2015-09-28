package com.oberasoftware.robo.api;

import com.oberasoftware.robo.api.motion.Motion;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface RobotController {
    boolean initialize();

    void shutdown();

    List<Servo> getServos();

    Servo getServo(String servoId);

    void executeMotion(Motion motion);

    void executeMotion(Motion motion, int repeats);
}
