package com.oberasoftware.robo.api.behavioural.humanoid;

public interface Leg extends JointChain {
    Hip getHip();

    Joint getKnee();

    Ankle getAnkle();
}
