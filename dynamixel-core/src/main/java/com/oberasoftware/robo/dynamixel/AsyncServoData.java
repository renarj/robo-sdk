package com.oberasoftware.robo.dynamixel;

import com.google.common.collect.Sets;
import com.oberasoftware.robo.api.servo.ServoData;
import com.oberasoftware.robo.api.servo.ServoDataManager;
import com.oberasoftware.robo.api.servo.ServoProperty;

import java.util.Map;
import java.util.Set;

/**
 * @author Renze de Vries
 */
public class AsyncServoData implements ServoData {

    private final ServoDataManager dataManager;

    private final String servoId;

    public AsyncServoData(ServoDataManager dataManager, String servoId) {
        this.dataManager = dataManager;
        this.servoId = servoId;
    }

    @Override
    public String getServoId() {
        return servoId;
    }

    @Override
    public Set<ServoProperty> getKeys() {
        return Sets.newHashSet(ServoProperty.values());
    }

    @Override
    public <T> T getValue(ServoProperty key) {
        return dataManager.readServoProperty(servoId, key);
    }

    @Override
    public boolean containsValue(ServoProperty key) {
        return false;
    }

    @Override
    public Map<ServoProperty, Object> getValues() {
        return dataManager.getCurrentValues(servoId);
    }
}
