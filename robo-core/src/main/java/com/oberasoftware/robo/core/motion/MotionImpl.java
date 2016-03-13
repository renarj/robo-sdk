package com.oberasoftware.robo.core.motion;

import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.api.motion.KeyFrame;

import java.util.List;

/**
 * @author Renze de Vries
 */
public class MotionImpl implements Motion {
    private final String name;
    private final String id;

    private final List<KeyFrame> keyFrames;

    private final String nextMotion;
    private final String exitMotion;

    public MotionImpl(String id, String name, String nextMotion, String exitMotion, List<KeyFrame> keyFrames) {
        this.id = id;
        this.name = name;
        this.keyFrames = keyFrames;
        this.nextMotion = nextMotion;
        this.exitMotion = exitMotion;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getNextMotion() {
        return nextMotion;
    }

    @Override
    public String getExitMotion() {
        return exitMotion;
    }

    @Override
    public String getName() {
        return name;
    }

    public List<KeyFrame> getKeyFrames() {
        return keyFrames;
    }

    @Override
    public String toString() {
        return "MotionImpl{" +
                "name='" + name + '\'' +
                ", keyFrames=" + keyFrames.size() +
                '}';
    }
}
