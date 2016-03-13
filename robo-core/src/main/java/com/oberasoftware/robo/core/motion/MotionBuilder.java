package com.oberasoftware.robo.core.motion;

import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.api.motion.KeyFrame;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Renze de Vries
 */
public class MotionBuilder {
    private final List<KeyFrame> keyFrames = new ArrayList<>();
    private final String name;

    private MotionBuilder(String name) {
        this.name = name;
    }

    public static MotionBuilder create(String motionName) {
        return new MotionBuilder(motionName);
    }

    public MotionBuilder addStep(KeyFrame keyFrame) {
        this.keyFrames.add(keyFrame);
        return this;
    }

    public Motion build() {
        return new MotionImpl(name, name, null, null, keyFrames);
    }
}
