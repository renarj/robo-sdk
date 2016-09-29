package com.oberasoftware.robo.api.servo.events;

import com.oberasoftware.robo.api.servo.ServoData;

/**
 * @author Renze de Vries
 */
public class ServoDataReceivedEvent implements ServoDataEvent {

    private final String servoId;
    private final ServoData updateData;

    public ServoDataReceivedEvent(String servoId, ServoData updateData) {
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
        return "ServoDataReceivedEvent{" +
                "servoId='" + servoId + '\'' +
                ", updateData=" + updateData +
                '}';
    }
}
