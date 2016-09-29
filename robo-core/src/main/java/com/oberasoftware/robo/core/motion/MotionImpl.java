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
    private String id;

    @JsonDeserialize(contentAs=KeyFrameImpl.class)
    private List<KeyFrame> keyFrames;

    private String nextMotion;
    private String exitMotion;

    public MotionImpl(String id, String name, String nextMotion, String exitMotion, List<KeyFrame> keyFrames) {
        this.id = id;
        this.name = name;
        this.keyFrames = keyFrames;
        this.nextMotion = nextMotion;
        this.exitMotion = exitMotion;
    }

    public MotionImpl() {
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setNextMotion(String nextMotion) {
        this.nextMotion = nextMotion;
    }

    @Override
    public String getNextMotion() {
        return nextMotion;
    }

    public void setExitMotion(String exitMotion) {
        this.exitMotion = exitMotion;
    }

    @Override
    public String getExitMotion() {
        return exitMotion;
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
