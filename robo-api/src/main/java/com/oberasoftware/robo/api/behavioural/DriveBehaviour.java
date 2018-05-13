package com.oberasoftware.robo.api.behavioural;

import com.oberasoftware.robo.api.commands.Scale;
import com.oberasoftware.robo.api.navigation.DirectionalInput;

import java.util.List;

/**
 * @author renarj
 */
public interface DriveBehaviour extends Behaviour {
    void drive(DirectionalInput input, Scale scale);

    void forward(int speed, Scale scale);

    void left(int speed, Scale scale);

    void backward(int speed, Scale scale);

    void right(int speed, Scale scale);

    void stop();

    List<Wheel> getWheels();
}
