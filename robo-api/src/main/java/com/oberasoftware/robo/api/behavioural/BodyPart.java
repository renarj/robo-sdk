package com.oberasoftware.robo.api.behavioural;

import java.util.List;

public interface BodyPart extends Behaviour {
    String getName();

    List<Joint> getJoints();

    Joint getJoint(String name);
}
