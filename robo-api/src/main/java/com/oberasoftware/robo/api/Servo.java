package com.oberasoftware.robo.api;

/**
 * @author Renze de Vries
 */
public interface Servo {
    String getId();

    ServoData getData();

    void moveTo(int position);

    void setSpeed(int speed);

    void setTorgueLimit(int torgueLimit);

    void enableTorgue();

    void disableTorgue();
}
