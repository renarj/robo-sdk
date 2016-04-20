package com.oberasoftware.robo.api.events;

/**
 * @author Renze de Vries
 */
public interface SensorEvent<T> extends RobotEvent {
    T getValue();
}
