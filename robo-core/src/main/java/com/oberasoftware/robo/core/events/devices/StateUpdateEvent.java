package com.oberasoftware.robo.core.events.devices;

import com.oberasoftware.robo.api.events.ItemEvent;
import com.oberasoftware.robo.api.model.State;

/**
 * @author renarj
 */
public class StateUpdateEvent implements ItemEvent {
    private final State state;
    private final String itemId;
    private final String controllerId;
    private final String label;

    public StateUpdateEvent(State state, String label) {
        this.state = state;
        this.itemId = state.getItemId();
        this.controllerId = state.getControllerId();
        this.label = label;
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    public String getControllerId() {
        return controllerId;
    }

    public State getState() {
        return state;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "StateUpdateEvent{" +
                "state=" + state +
                ", itemId='" + itemId + '\'' +
                ", controllerId='" + controllerId + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
