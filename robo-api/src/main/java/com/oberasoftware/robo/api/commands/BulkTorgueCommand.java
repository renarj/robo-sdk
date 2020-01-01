package com.oberasoftware.robo.api.commands;

import com.oberasoftware.base.event.Event;

import java.util.ArrayList;
import java.util.List;

public class BulkTorgueCommand implements Event {
    private final boolean torgueState;
    private final List<String> servos;

    public BulkTorgueCommand(boolean torgueState) {
        this.torgueState = torgueState;
        this.servos = new ArrayList<>();
    }

    public BulkTorgueCommand(boolean torgueState, List<String> servos) {
        this.torgueState = torgueState;
        this.servos = servos;
    }

    public boolean isTorgueState() {
        return torgueState;
    }

    public List<String> getServos() {
        return servos;
    }

    @Override
    public String toString() {
        return "BulkTorgueCommand{" +
                "torgueState=" + torgueState +
                ", servos=" + servos +
                '}';
    }
}
