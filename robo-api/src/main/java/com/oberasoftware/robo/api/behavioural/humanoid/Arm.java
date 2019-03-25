package com.oberasoftware.robo.api.behavioural.humanoid;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Arm extends JointChain {
    @JsonIgnore
    Shoulder getShoulder();

    @JsonIgnore
    Joint getElbow();

    @JsonIgnore
    Joint getHand();
}
