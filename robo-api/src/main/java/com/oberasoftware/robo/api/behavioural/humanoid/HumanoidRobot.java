package com.oberasoftware.robo.api.behavioural.humanoid;

import com.oberasoftware.robo.api.behavioural.BehaviouralRobot;

import java.util.List;
import java.util.Optional;

public interface HumanoidRobot extends BehaviouralRobot, ChainSet {
    Optional<ChainSet> getChainSet(String name);

    List<ChainSet> getChainSets();
}
