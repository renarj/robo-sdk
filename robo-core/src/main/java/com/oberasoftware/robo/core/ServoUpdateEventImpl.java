package com.oberasoftware.robo.core;

import com.oberasoftware.robo.api.ServoData;
import com.oberasoftware.robo.api.ServoUpdateEvent;

/**
 * @author Renze de Vries
 */
public class ServoUpdateEventImpl implements ServoUpdateEvent {

    private final String servoId;
    private final ServoData updateData;

    public ServoUpdateEventImpl(String servoId, ServoData updateData) {
        this.servoId = servoId;
        this.updateData = updateData;
    }

    @Override
    public String getServoId() {
        return servoId;
    }

    @Override
    public ServoData getServoData() {
        return updateData;
    }

    @Override
    public String toString() {
        return "ServoUpdateEventImpl{" +
                "servoId='" + servoId + '\'' +
                ", updateData=" + updateData +
                '}';
    }
}
