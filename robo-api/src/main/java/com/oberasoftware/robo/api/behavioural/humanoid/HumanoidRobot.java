package com.oberasoftware.robo.api.behavioural.humanoid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.behavioural.BehaviouralRobot;

import java.util.List;
import java.util.Optional;

public interface HumanoidRobot extends BehaviouralRobot, ChainSet {
    Optional<ChainSet> getChainSet(String name);

    List<ChainSet> getChainSets();

    List<ChainSet> getChainSets(boolean includeChildren);

    Robot getRobotCore();

    MotionControl getMotionControl();

    @JsonIgnore
    Head getHead();

    @JsonIgnore
    Torso getTorso();

    @JsonIgnore
    Legs getLegs();
}
