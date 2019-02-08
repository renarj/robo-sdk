package com.oberasoftware.robo.api.behavioural.humanoid;

public interface Arm extends JointChain {
    Shoulder getShoulder();

    Joint getElbow();

    Hand getHand();
}
