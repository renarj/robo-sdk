package com.oberasoftware.robo.api;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface RobotController {
    boolean initialize();

    void shutdown();

    List<Servo> getServos();

    Servo getServo(String servoId);
}
