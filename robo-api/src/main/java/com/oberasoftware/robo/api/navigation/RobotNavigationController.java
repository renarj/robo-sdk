package com.oberasoftware.robo.api.navigation;

import com.oberasoftware.robo.api.behavioural.Behaviour;

public interface RobotNavigationController extends Behaviour {
    void move(DirectionalInput input);

    DirectionalInput getNavigationDirections();

    void stop();
}
