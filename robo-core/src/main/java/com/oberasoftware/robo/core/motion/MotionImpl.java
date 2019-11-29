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
    private List<KeyFrame> frames;

    public MotionImpl(String name, List<KeyFrame> frames) {
        this.name = name;
        this.frames = frames;
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

    public void setFrames(List<KeyFrame> frames) {
        this.frames = frames;
    }

    public List<KeyFrame> getFrames() {
        return Lists.newArrayList(frames);
    }

    @Override
    public String toString() {
        return "MotionImpl{" +
                "name='" + name + '\'' +
                ", keyFrames=" + frames.size() +
                '}';
    }
}
