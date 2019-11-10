package com.oberasoftware.robo.api.servo;

import java.util.Map;
import java.util.Set;

/**
 * @author Renze de Vries
 */
public interface ServoData {
    String getServoId();

    Set<ServoProperty> getKeys();

    <T> T getValue(ServoProperty key);

    boolean containsValue(ServoProperty key);

    Map<ServoProperty, Object> getValues();
}
