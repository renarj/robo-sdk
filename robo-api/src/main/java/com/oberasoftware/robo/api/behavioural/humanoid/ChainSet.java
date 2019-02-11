package com.oberasoftware.robo.api.behavioural.humanoid;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public interface ChainSet extends JointChain {
    List<JointChain> getChains();
}
