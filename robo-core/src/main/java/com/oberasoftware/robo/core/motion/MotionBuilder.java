package com.oberasoftware.robo.core.motion;

import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.api.motion.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Renze de Vries
 */
public class MotionBuilder {
    private final List<Step> steps = new ArrayList<>();
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

    public MotionBuilder addStep(Step step) {
        this.steps.add(step);
        return this;
    }

    public Motion build() {
        return new MotionImpl(name, repeats, steps);
    }
}
