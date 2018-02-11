package com.oberasoftware.robo.dynamixel.web.test;

public class TestServo {
    private final String servoId;

    public TestServo(String servoId) {
        this.servoId = servoId;
    }

    public String getServoId() {
        return servoId;
    }

    @Override
    public String toString() {
        return "Servo{" +
                "servoId='" + servoId + '\'' +
                '}';
    }
}
