package com.oberasoftware.robo.api.behavioural.humanoid;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Head extends ChainSet {
    @JsonIgnore
    Joint getPitch();

    @JsonIgnore
    Joint getYaw();
}
