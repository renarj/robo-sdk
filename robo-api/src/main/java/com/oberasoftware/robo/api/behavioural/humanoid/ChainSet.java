package com.oberasoftware.robo.api.behavioural.humanoid;

import java.util.List;

public interface ChainSet extends JointChain {
    List<JointChain> getJointChains();
}
