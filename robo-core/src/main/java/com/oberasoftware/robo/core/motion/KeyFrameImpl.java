package com.oberasoftware.robo.core.motion;

import com.oberasoftware.robo.api.motion.ServoStep;
import com.oberasoftware.robo.api.motion.KeyFrame;

import java.util.*;

/**
 * @author Renze de Vries
 */
public class KeyFrameImpl implements KeyFrame {

    private final Map<String, ServoStep> servoSteps = new LinkedHashMap<>();
    private final long timeInMs;
    private final String keyFrameId;

    public KeyFrameImpl(String keyFrameId, long timeInMs) {
        this.keyFrameId = keyFrameId;
        this.timeInMs = timeInMs;
    }

    @Override
    public String getKeyFrameId() {
        return this.keyFrameId;
    }

    @Override
    public long getTimeInMs() {
        return timeInMs;
    }

    @Override
    public Set<String> getServoIds() {
        return servoSteps.keySet();
    }

    @Override
    public List<ServoStep> getServoSteps() {
        return new ArrayList<>(servoSteps.values());
    }

    public void addServoStep(ServoStep servoStep) {
        this.servoSteps.put(servoStep.getServoId(), servoStep);
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
