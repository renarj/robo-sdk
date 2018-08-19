package com.oberasoftware.robo.api.behavioural;

import java.util.List;
import java.util.Optional;

public interface HumanoidRobot extends BehaviouralRobot {
    List<BodyPart> getBodyParts();

    Optional<BodyPart> getBodyPart(String name);

    List<Joint> getJoints();

    Optional<Joint> getJoint(String name);
}
