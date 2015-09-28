package com.oberasoftware.robo.core.motion;

import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.api.motion.Step;

import java.util.List;

/**
 * @author Renze de Vries
 */
public class MotionImpl implements Motion {
    private final String name;
    private final int repeats;

    private final List<Step> steps;

    public MotionImpl(String name, int repeats, List<Step> steps) {
        this.name = name;
        this.repeats = repeats;
        this.steps = steps;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getRepeats() {
        return repeats;
    }

    @Override
    public List<Step> getSteps() {
        return steps;
    }

    @Override
    public String toString() {
        return "MotionImpl{" +
                "name='" + name + '\'' +
                ", repeats=" + repeats +
                ", steps=" + steps +
                '}';
    }
}
