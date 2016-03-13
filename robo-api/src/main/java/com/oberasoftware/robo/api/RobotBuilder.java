package com.oberasoftware.robo.api;

/**
 * @author Renze de Vries
 */
public class RobotBuilder {

    public RobotBuilder motionEngine(MotionEngine engine) {
        return this;
    }

    public RobotBuilder servoDriver(ServoDriver driver) {
        return this;
    }

    public Robot build() {
        return null;
    }
}
