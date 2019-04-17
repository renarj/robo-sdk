package com.oberasoftware.robo.api.behavioural.humanoid;

import com.oberasoftware.robo.api.behavioural.Behaviour;

public interface Joint extends Behaviour {
    String getID();

    String getName();

    String getJointType();

    int getMaxDegrees();

    int getMinDegrees();

    boolean moveTo(int degrees);
}
