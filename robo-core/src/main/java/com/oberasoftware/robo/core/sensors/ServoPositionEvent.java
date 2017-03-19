package com.oberasoftware.robo.core.sensors;

import com.oberasoftware.robo.api.events.SensorEvent;

/**
 * @author Renze de Vries
 */
public class ServoPositionEvent implements SensorEvent<PositionValue> {

    private final String controllerId;
    private final String source;
    private final String servoId;

    private final PositionValue positionValue;

    public ServoPositionEvent(String controllerId, String source, String servoId, PositionValue positionValue) {
        this.controllerId = controllerId;
        this.source = source;
        this.servoId = servoId;
        this.positionValue = positionValue;
    }

    @Override
    public PositionValue getValue() {
        return positionValue;
    }

    @Override
    public String getControllerId() {
        return controllerId;
    }

    @Override
    public String getItemId() {
        return source;
    }

    @Override
    public String getLabel() {
        return servoId;
    }

    @Override
    public String toString() {
        return "ServoPositionEvent{" +
                "controllerId='" + controllerId + '\'' +
                ", source='" + source + '\'' +
                ", servoId='" + servoId + '\'' +
                ", positionValue=" + positionValue +
                '}';
    }
}
