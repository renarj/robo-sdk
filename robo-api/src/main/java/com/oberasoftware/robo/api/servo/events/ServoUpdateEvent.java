package com.oberasoftware.robo.api.servo.events;

import com.oberasoftware.robo.api.servo.ServoData;

/**
 * @author Renze de Vries
 */
public class ServoUpdateEvent implements ServoDataEvent {
    private final String servoId;
    private final ServoData updateData;

    public ServoUpdateEvent(String servoId, ServoData updateData) {
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
        return "ServoUpdateEvent{" +
                "servoId='" + servoId + '\'' +
                ", updateData=" + updateData +
                '}';
    }

}
