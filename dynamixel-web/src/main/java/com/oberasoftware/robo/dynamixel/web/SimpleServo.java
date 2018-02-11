package com.oberasoftware.robo.dynamixel.web;

import com.oberasoftware.robo.api.servo.Servo;
import com.oberasoftware.robo.api.servo.ServoProperty;

/**
 * @author Renze de Vries
 */
public class SimpleServo {
    private final String servoId;
    private final int speed;
    private final int position;
    private final int torgue;
    private final int temperature;
    private final double voltage;
    private final int minPosition;
    private final int maxPosition;

    public SimpleServo(String servoId, Integer speed, Integer position, Integer torgue, Integer temperature, Double voltage) {
        this.servoId = servoId;
        this.speed = speed != null ? speed : 0;
        this.position = position != null ? position : 0;
        this.torgue = torgue != null ? torgue : 0;
        this.temperature = temperature != null ? temperature : 0;
        this.voltage = voltage != null ? voltage : 0.0;
        this.minPosition = 0;
        this.maxPosition = 1023;
    }

    public SimpleServo(Servo servo) {
        this.servoId = servo.getId();
        this.speed = servo.getData().getValue(ServoProperty.SPEED);
        this.position = servo.getData().getValue(ServoProperty.POSITION);
        Integer t = servo.getData().getValue(ServoProperty.TEMPERATURE);
        Double c = servo.getData().getValue(ServoProperty.VOLTAGE);
        this.minPosition = servo.getData().getValue(ServoProperty.MIN_ANGLE_LIMIT);
        this.maxPosition = servo.getData().getValue(ServoProperty.MAX_ANGLE_LIMIT);
        this.temperature = t != null ? t : 0;
        this.voltage = c != null ? c : 0.0;
        this.torgue = 0;
    }

    public String getServoId() {
        return servoId;
    }

    public int getTorgue() {
        return torgue;
    }

    public int getSpeed() {
        return speed;
    }

    public int getTemperature() {
        return temperature;
    }

    public double getVoltage() {
        return voltage;
    }

    public int getMinPosition() {
        return minPosition;
    }

    public int getMaxPosition() {
        return maxPosition;
    }

    public int getPosition() {
        return position;
    }
}
