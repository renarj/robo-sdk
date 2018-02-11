package com.oberasoftware.robo.core.events.items;

import com.oberasoftware.robo.api.events.ItemValueEvent;
import com.oberasoftware.robo.api.model.Value;

/**
 * @author Renze de Vries
 */
public class ItemNumericValue implements ItemValueEvent {
    private final String itemId;
    private final Value value;
    private final String label;

    public ItemNumericValue(String itemId, Value value, String label) {
        this.itemId = itemId;
        this.value = value;
        this.label = label;
    }

    @Override
    public String getItemId() {
        return this.itemId;
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
