package com.oberasoftware.robo.api.behavioural;


import java.util.List;

/**
 * @author renarj
 */
public interface DriveTrain extends Behaviour {
    void forward(int speed);

    void backward(int speed);

    void stop();

    List<Wheel> getWheels();
}
