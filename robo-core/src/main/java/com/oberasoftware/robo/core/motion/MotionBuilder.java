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
    private final int repeats;

    private MotionBuilder(String name, int repeats) {
        this.name = name;
        this.repeats = repeats;
    }

    public static MotionBuilder create(String motionName) {
        return new MotionBuilder(motionName, 0);
    }

    public static MotionBuilder create(String motionName, int repeats) {
        return new MotionBuilder(motionName, repeats);
    }

    public MotionBuilder addStep(KeyFrame keyFrame) {
        this.keyFrames.add(keyFrame);
        return this;
    }

    public Motion build() {
        return new MotionImpl(name, name, repeats, null, null, keyFrames);
    }
}
