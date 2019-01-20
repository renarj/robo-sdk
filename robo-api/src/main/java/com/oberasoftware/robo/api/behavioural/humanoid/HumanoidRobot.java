package com.oberasoftware.robo.api.behavioural.humanoid;

import com.oberasoftware.robo.api.behavioural.BehaviouralRobot;

import java.util.List;
import java.util.Optional;

public interface HumanoidRobot extends BehaviouralRobot {
    List<JointChain> getChainSets();

    Optional<ChainSet> getChainSet(String name);

    List<Joint> getJoints();

    Optional<Joint> getJoint(String name);
}
