package com.oberasoftware.robo.core;

import com.oberasoftware.robo.api.servo.ServoData;
import com.oberasoftware.robo.api.servo.ServoProperty;

import java.util.Map;
import java.util.Set;

/**
 * @author Renze de Vries
 */
public class ServoDataImpl implements ServoData {

    private final Map<ServoProperty, Object> data;
    private final String servoId;

    public ServoDataImpl(String servoId, Map<ServoProperty, Object> data) {
        this.data = data;
        this.servoId = servoId;
    }

    @Override
    public String getServoId() {
        return servoId;
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
    public boolean containsValue(ServoProperty key) {
        return data.containsKey(key);
    }

    @Override
    public String toString() {
        return "ServoDataImpl{" +
                "data=" + data +
                ", servoId='" + servoId + '\'' +
                '}';
    }
}
