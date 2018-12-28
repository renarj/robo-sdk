package com.oberasoftware.robo.api.servo;

import com.oberasoftware.robo.api.commands.Scale;

/**
 * @author Renze de Vries
 */
public interface Servo extends DynamixelDevice {

    ServoData getData();

    void moveTo(int position, Scale scale);

    void setSpeed(int speed, Scale scale);

    void setTorgueLimit(int torgueLimit);

    void enableTorgue();

    void disableTorgue();
}
