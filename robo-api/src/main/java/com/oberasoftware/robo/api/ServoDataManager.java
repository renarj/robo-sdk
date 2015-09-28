package com.oberasoftware.robo.api;

import java.util.Map;

/**
 * @author Renze de Vries
 */
public interface ServoDataManager {
    Map<ServoProperty, Object> getCurrentValues(String servoId);

    <T> T readServoProperty(String servoId, ServoProperty property);

    boolean readServoProperties(String servoId, ServoProperty... properties);
}
