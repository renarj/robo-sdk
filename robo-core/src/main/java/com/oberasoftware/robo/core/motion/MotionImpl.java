package com.oberasoftware.robo.core.motion;

import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.api.motion.Step;

import java.util.List;

/**
 * @author Renze de Vries
 */
public class MotionImpl implements Motion {
    private final String name;
    private final String id;
    private final int repeats;

    private final List<Step> steps;

    private final String nextMotion;
    private final String exitMotion;

    public MotionImpl(String id, String name, int repeats, String nextMotion, String exitMotion, List<Step> steps) {
        this.id = id;
        this.name = name;
        this.repeats = repeats;
        this.steps = steps;
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
