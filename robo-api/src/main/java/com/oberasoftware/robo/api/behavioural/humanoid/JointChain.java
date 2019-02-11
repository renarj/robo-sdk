package com.oberasoftware.robo.api.behavioural.humanoid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oberasoftware.robo.api.behavioural.Behaviour;

import java.util.List;
import java.util.Optional;

public interface JointChain extends Behaviour {
    @JsonIgnore
    List<Joint> getJoints();

    default Optional<Joint> getJoint(String name) {
        return getJoints().stream()
                .filter(j -> j.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    String getName();
}
