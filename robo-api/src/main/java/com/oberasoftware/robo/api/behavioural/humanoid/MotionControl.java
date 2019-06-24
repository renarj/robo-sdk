package com.oberasoftware.robo.api.behavioural.humanoid;

import com.oberasoftware.robo.api.behavioural.Behaviour;

import java.util.List;

public interface MotionControl extends Behaviour {
    JointData getJointData(String jointId);

    List<JointData> getJointsData();

    Joint getJoint(String jointId);

    List<Joint> getJoints();

    void setJointPosition(JointData position);

    void setJointPositions(List<JointData> jointPositions);
}
