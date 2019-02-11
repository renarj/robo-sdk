package com.oberasoftware.robo.api.behavioural.humanoid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oberasoftware.robo.api.behavioural.BehaviouralRobot;

import java.util.List;
import java.util.Optional;

public interface HumanoidRobot extends BehaviouralRobot, ChainSet {
    Optional<ChainSet> getChainSet(String name);

    @JsonIgnore
    List<ChainSet> getChainSets();

    Head getHead();

    Torso getTorso();

    Legs getLegs();
}
