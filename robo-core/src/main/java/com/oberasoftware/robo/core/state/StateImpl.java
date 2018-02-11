package com.oberasoftware.robo.core.state;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Lists;
import com.oberasoftware.robo.api.model.State;
import com.oberasoftware.robo.api.model.StateItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author renarj
 */
public class StateImpl implements State {

    private String controllerId;
    private String itemId;

    @JsonDeserialize(contentAs = StateItemImpl.class)
    private List<StateItem> stateItems = new ArrayList<>();

    public StateImpl(String controllerId, String itemId, List<StateItem> stateItems) {
        this.controllerId = controllerId;
        this.itemId = itemId;
        this.stateItems = stateItems;
    }

    public StateImpl() {
    }

    @Override
    public String getControllerId() {
        return this.controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public List<StateItem> getStateItems() {
        return Lists.newArrayList(stateItems);
    }

    public void setStateItems(List<StateItem> stateItems) {
        this.stateItems = stateItems;
    }

    @Override
    public StateItem getStateItem(String label) {
        Optional<StateItem> r = stateItems.stream().filter(s -> s.getLabel().equalsIgnoreCase(label)).findAny();
        return r.isPresent() ? r.get() : null;
    }

    @Override
    public String toString() {
        return "StateImpl{" +
                "itemId='" + itemId + '\'' +
                ", stateItems=" + stateItems +
                '}';
    }
}
