package com.oberasoftware.robo.api.behavioural.humanoid;

import com.oberasoftware.robo.api.behavioural.Behaviour;

public interface Joint extends Behaviour {
    String getName();

    String getJointType();

    boolean moveTo(int degrees);
}
