package com.oberasoftware.robo.api.behavioural.humanoid;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Leg extends JointChain {
    @JsonIgnore
    Hip getHip();

    @JsonIgnore
    Joint getKnee();

    @JsonIgnore
    Ankle getAnkle();
}
