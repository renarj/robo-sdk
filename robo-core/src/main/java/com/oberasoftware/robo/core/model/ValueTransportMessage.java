package com.oberasoftware.robo.core.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.oberasoftware.robo.api.model.Value;

/**
 * @author Renze de Vries
 */
public class ValueTransportMessage {

    @JsonDeserialize(as = ValueImpl.class)
    private Value value;

    private String controllerId;
    private String channelId;
    private String label;

    public ValueTransportMessage(Value value, String controllerId, String channelId, String label) {
        this.value = value;
        this.controllerId = controllerId;
        this.channelId = channelId;
        this.label = label;
    }

    public ValueTransportMessage() {
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "ValueTransportMessage{" +
                "value=" + value +
                ", controllerId='" + controllerId + '\'' +
                ", channelId='" + channelId + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
