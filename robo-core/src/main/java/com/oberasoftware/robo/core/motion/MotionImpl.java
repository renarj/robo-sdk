package com.oberasoftware.robo.core.motion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Lists;
import com.oberasoftware.robo.api.motion.KeyFrame;
import com.oberasoftware.robo.api.motion.Motion;

import java.util.List;

/**
 * @author Renze de Vries
 */
public class MotionImpl implements Motion {
    private String name;

    @JsonDeserialize(contentAs=KeyFrameImpl.class)
    private List<KeyFrame> keyFrames;

    public MotionImpl(String name, List<KeyFrame> keyFrames) {
        this.name = name;
        this.keyFrames = keyFrames;
    }

    public MotionImpl() {
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setKeyFrames(List<KeyFrame> keyFrames) {
        this.keyFrames = keyFrames;
    }

    public List<KeyFrame> getKeyFrames() {
        return Lists.newArrayList(keyFrames);
    }

    @Override
    public String toString() {
        return "MotionImpl{" +
                "name='" + name + '\'' +
                ", keyFrames=" + keyFrames.size() +
                '}';
    }
}
