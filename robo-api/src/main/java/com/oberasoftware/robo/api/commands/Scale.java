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

    public boolean isValid(double value) {
        return value > min && value < max;
    }

    public int convertToScale(int value, Scale targetScale) {
        if(isValid(value)) {
            double rangeSource = max + Math.abs(min);
            double rangeTarget = targetScale.getMax() + Math.abs(targetScale.getMin());

            double factor = rangeTarget / rangeSource;

            int correctedValue = value + Math.abs(getMin());

            LOG.debug("Range Source: {} target: {} factor: {} correctedValue: {}", rangeSource, rangeTarget, factor, correctedValue);

            return (int)(factor * correctedValue) - Math.abs(targetScale.getMin());
        } else {
            LOG.warn("Scale conversion issue source scale: {} value: {} target scale: {}", this, value, targetScale);
            new Exception().printStackTrace();
            return value;
        }
    }

    public double convertToScale(double value, Scale targetScale) {
        if(isValid(value)) {
            double rangeSource = max + Math.abs(min);
            double rangeTarget = targetScale.getMax() + Math.abs(targetScale.getMin());

            double factor = rangeTarget / rangeSource;

            double correctedValue = value + Math.abs(getMin());

            LOG.debug("Range Source: {} target: {} factor: {} correctedValue: {}", rangeSource, rangeTarget, factor, correctedValue);

            return (factor * correctedValue) - Math.abs(targetScale.getMin());
        } else {
            LOG.warn("Scale conversion issue source scale: {} value: {} target scale: {}", this, value, targetScale);
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
