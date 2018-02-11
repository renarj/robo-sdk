package com.oberasoftware.robo.api.model;

/**
 * @author renarj
 */
public interface Value {
    com.oberasoftware.robo.api.model.VALUE_TYPE getType();

    <T> T getValue();

    String asString();
}
