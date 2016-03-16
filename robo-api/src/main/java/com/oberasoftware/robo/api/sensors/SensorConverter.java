package com.oberasoftware.robo.api.sensors;

/**
 * @author Renze de Vries
 */
public interface SensorConverter<S, T> {
    T convert(S input);
}
