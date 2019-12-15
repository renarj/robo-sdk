package com.oberasoftware.robo.api.commands;

import com.oberasoftware.base.event.Event;

public class BulkTorgueCommand implements Event {
    private final boolean torgueState;

    public BulkTorgueCommand(boolean torgueState) {
        this.torgueState = torgueState;
    }

    public boolean isTorgueState() {
        return torgueState;
    }

    @Override
    public String toString() {
        return "BulkTorgueCommand{" +
                "torgueState=" + torgueState +
                '}';
    }
}
