package com.oberasoftware.robo.dynamixel.web.commands;

import java.util.List;

public class TorgueCommand {
    private List<String> servos;
    private boolean enable;

    public TorgueCommand(List<String> servos, boolean enable) {
        this.servos = servos;
        this.enable = enable;
    }

    public TorgueCommand() {
    }

    public List<String> getServos() {
        return servos;
    }

    public void setServos(List<String> servos) {
        this.servos = servos;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "TorgueCommand{" +
                "servos=" + servos +
                ", enable=" + enable +
                '}';
    }
}
