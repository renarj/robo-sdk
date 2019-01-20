package com.oberasoftware.robo.api.behavioural.humanoid;

import com.oberasoftware.robo.api.behavioural.Behaviour;

import java.util.List;

public interface JointChain extends Behaviour {
    List<Joint> getJoints();

    String getName();
}
