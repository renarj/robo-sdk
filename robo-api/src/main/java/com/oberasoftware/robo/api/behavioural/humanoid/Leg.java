package com.oberasoftware.robo.api.behavioural.humanoid;

public interface Leg extends ChainSet {
    Hip getHip();

    Joint getKnee();

    Ankle getAnkle();
}
