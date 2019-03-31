package com.oberasoftware.robo.api.behavioural.humanoid;

import com.oberasoftware.robo.api.behavioural.Behaviour;

import java.util.List;

public interface MotionControl extends Behaviour {
    JointData getJoint(String jointId);

    List<JointData> getJoints();

    void setJointPosition(JointData position);

    void setJointPositions(List<JointData> jointPositions);
}
