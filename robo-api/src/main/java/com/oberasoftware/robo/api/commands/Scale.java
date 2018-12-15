package com.oberasoftware.robo.api.commands;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class Scale {
    private static final Logger LOG = getLogger(Scale.class);

    private final int min;
    private final int max;

    public Scale(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public boolean isAbsolute() {
        return min == max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public boolean isValid(int value) {
        return value >= min && value <= max;
    }

    public int convertToScale(int value, Scale targetScale) {
        if(isValid(value)) {
            double factor = (double)targetScale.getMax() / (double)getMax();

            return (int)(factor * value);
        } else {
            LOG.debug("Scale conversion issue source scale: {} value: {} target scale: {}", this, value, targetScale);
            return value;
        }
    }

    @Override
    public String toString() {
        return "Scale{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
