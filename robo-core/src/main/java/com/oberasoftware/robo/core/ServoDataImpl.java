package com.oberasoftware.robo.core;

import com.oberasoftware.robo.api.ServoData;
import com.oberasoftware.robo.api.ServoProperty;

import java.util.Map;
import java.util.Set;

/**
 * @author Renze de Vries
 */
public class ServoDataImpl implements ServoData {

    private final Map<ServoProperty, Object> data;

    public ServoDataImpl(Map<ServoProperty, Object> data) {
        this.data = data;
    }

    @Override
    public Set<ServoProperty> getKeys() {
        return data.keySet();
    }

    @Override
    public <T> T getValue(ServoProperty key) {
        return (T) data.get(key);
    }

    @Override
    public Map<ServoProperty, Object> getValues() {
        return data;
    }

    @Override
    public String toString() {
        return "ServoDataImpl{" +
                "data=" + data +
                '}';
    }
}
