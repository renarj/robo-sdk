package com.oberasoftware.robo.service.mock;

import com.oberasoftware.robo.api.servo.Servo;
import com.oberasoftware.robo.api.servo.ServoData;
import com.oberasoftware.robo.api.servo.ServoProperty;
import com.oberasoftware.robo.core.ServoDataImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Renze de Vries
 */
public class MockServo implements Servo {

    private final String id;
    private int speed;
    private int position;
    private boolean torgue;

    public MockServo(String id, int speed, int position, boolean torgue) {
        this.id = id;
        this.speed = speed;
        this.position = position;
        this.torgue = torgue;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ServoData getData() {
        Map<ServoProperty, Object> servoData = new HashMap<>();
        servoData.put(ServoProperty.POSITION, position);
        servoData.put(ServoProperty.SPEED, speed);
        servoData.put(ServoProperty.TORGUE, torgue);

        return new ServoDataImpl(servoData);
    }

    @Override
    public void moveTo(int position) {
        this.position = position;
    }

    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public void setTorgueLimit(int torgueLimit) {
    }

    @Override
    public void enableTorgue() {
        this.torgue = true;
    }

    @Override
    public void disableTorgue() {
        this.torgue = false;
    }
}
