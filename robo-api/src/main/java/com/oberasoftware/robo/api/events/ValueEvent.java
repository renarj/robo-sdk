package com.oberasoftware.robo.api.events;

import com.oberasoftware.home.api.types.Value;

/**
 * @author Renze de Vries
 */
public class ValueEvent implements RobotEvent {
    private final String controllerId;
    private final String itemId;
    private final String label;
    private final Value value;

    public ValueEvent(String controllerId, String itemId, String label, Value value) {
        this.controllerId = controllerId;
        this.itemId = itemId;
        this.label = label;
        this.value = value;
    }

    @Override
    public String getControllerId() {
        return controllerId;
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ValueEvent{" +
                "controllerId='" + controllerId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", label='" + label + '\'' +
                ", value=" + value +
                '}';
    }
}
