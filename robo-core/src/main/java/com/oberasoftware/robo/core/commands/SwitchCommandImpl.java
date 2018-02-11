package com.oberasoftware.robo.core.commands;

import com.oberasoftware.robo.api.commands.SwitchCommand;
import com.oberasoftware.robo.core.types.OnOffValue;
import com.oberasoftware.robo.api.model.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * @author renarj
 */
public class SwitchCommandImpl implements SwitchCommand {

    private final String controllerId;
    private final String itemId;
    private final STATE state;

    public SwitchCommandImpl(String controllerId, String itemId, STATE state) {
        this.controllerId = controllerId;
        this.itemId = itemId;
        this.state = state;
    }

    @Override
    public String getControllerId() {
        return controllerId;
    }

    @Override
    public STATE getState() {
        return state;
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    @Override
    public String toString() {
        return "SwitchCommandImpl{" +
                "state=" + state +
                ", itemId='" + itemId + '\'' +
                '}';
    }

    @Override
    public Value getValue(String property) {
        return getValues().get(property);
    }

    @Override
    public Map<String, Value> getValues() {
        Map<String, Value> valueMap = new HashMap<>();
        valueMap.put(OnOffValue.LABEL, new OnOffValue(state == STATE.ON));

        return valueMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SwitchCommandImpl that = (SwitchCommandImpl) o;

        if (!itemId.equals(that.itemId)) return false;
        return state == that.state;

    }

    @Override
    public int hashCode() {
        int result = itemId.hashCode();
        result = 31 * result + state.hashCode();
        return result;
    }
}
