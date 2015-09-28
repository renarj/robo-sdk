package com.oberasoftware.robo.api;

import java.util.Map;
import java.util.Set;

/**
 * @author Renze de Vries
 */
public interface ServoData {
    Set<ServoProperty> getKeys();

    <T> T getValue(ServoProperty key);

    Map<ServoProperty, Object> getValues();
}
