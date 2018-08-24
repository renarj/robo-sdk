package com.oberasoftware.robo.api.behavioural.humanoid;

import com.oberasoftware.robo.api.behavioural.Behaviour;

import java.util.List;

public interface BodyPart extends Behaviour {
    String getName();

    List<Joint> getJoints();

    Joint getJoint(String name);
}
