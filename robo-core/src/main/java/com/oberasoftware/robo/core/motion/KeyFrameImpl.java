package com.oberasoftware.robo.core.motion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Lists;
import com.oberasoftware.robo.api.motion.JointTarget;
import com.oberasoftware.robo.api.motion.KeyFrame;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Renze de Vries
 */
public class KeyFrameImpl implements KeyFrame {

    @JsonDeserialize(contentAs = JointTargetImpl.class)
    private List<JointTarget> jointTargets = new ArrayList<>();

    private long timeInMs;
    private String keyFrameId;

    public KeyFrameImpl(String keyFrameId, long timeInMs) {
        this.keyFrameId = keyFrameId;
        this.timeInMs = timeInMs;
    }

    public KeyFrameImpl() {
    }

    public void setKeyFrameId(String keyFrameId) {
        this.keyFrameId = keyFrameId;
    }

    @Override
    public String getKeyFrameId() {
        return this.keyFrameId;
    }

    public void setTimeInMs(long timeInMs) {
        this.timeInMs = timeInMs;
    }

    @Override
    public long getTimeInMs() {
        return timeInMs;
    }

    @Override
    public List<JointTarget> getJointTargets() {
        return jointTargets;
    }

    public void setJointTargets(List<JointTarget> jointTargets) {
        this.jointTargets = jointTargets;
    }

    public void addServoStep(JointTarget servoStep) {
        this.jointTargets.add(servoStep);
    }

    @Override
    @JsonIgnore
    public List<KeyFrame> getFrames() {
        return Lists.newArrayList(this);
    }

    @Override
    public String toString() {
        return "KeyFrameImpl{" +
                "jointTargets=" + jointTargets +
                ", timeInMs=" + timeInMs +
                ", keyFrameId='" + keyFrameId + '\'' +
                '}';
    }
}
