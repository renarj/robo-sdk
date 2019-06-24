package com.oberasoftware.robo.core.motion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.oberasoftware.robo.api.motion.ServoStep;
import com.oberasoftware.robo.api.motion.KeyFrame;

import java.util.*;

/**
 * @author Renze de Vries
 */
public class KeyFrameImpl implements KeyFrame {

    @JsonDeserialize(contentAs = ServoStepImpl.class)
    private List<ServoStep> servoSteps = new ArrayList<>();

    private Set<String> servoIds = new HashSet<>();

    private long timeInMs;
    private String keyFrameId;
    private String motionId;

    public KeyFrameImpl(String motionId, String keyFrameId, long timeInMs) {
        this.motionId = motionId;
        this.keyFrameId = keyFrameId;
        this.timeInMs = timeInMs;
    }

    public KeyFrameImpl() {
    }

    @Override
    public String getMotionId() {
        return motionId;
    }

    public void setMotionId(String motionId) {
        this.motionId = motionId;
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
    public Set<String> getServoIds() {
        return servoIds;
    }

    public void setServoIds(Set<String> servoIds) {
        this.servoIds = servoIds;
    }

    @Override
    public List<ServoStep> getServoSteps() {
        return servoSteps;
    }

    public void setServoSteps(List<ServoStep> servoSteps) {
        this.servoSteps = servoSteps;
    }

    public void addServoStep(ServoStep servoStep) {
        this.servoSteps.add(servoStep);
        this.servoIds.add(servoStep.getServoId());
    }

    @Override
    public String toString() {
        return "KeyFrameImpl{" +
                "servoSteps=" + servoSteps +
                ", timeInMs=" + timeInMs +
                ", keyFrameId='" + keyFrameId + '\'' +
                '}';
    }
}
