package com.oberasoftware.robo.core.model;

import java.util.List;

/**
 * @author Renze de Vries
 */
public class ControllerImpl implements com.oberasoftware.robo.api.model.Controller {
    private String controllerId;
    private long lastSeen;
    private List<com.oberasoftware.robo.api.model.Device> devices;

    public ControllerImpl(String controllerId) {
        this.controllerId = controllerId;
    }

    @Override
    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    @Override
    public List<com.oberasoftware.robo.api.model.Device> getDevices() {
        return devices;
    }

    public void setDevices(List<com.oberasoftware.robo.api.model.Device> devices) {
        this.devices = devices;
    }

    @Override
    public String getId() {
        return controllerId;
    }

    @Override
    public String toString() {
        return "ControllerImpl{" +
                "controllerId='" + controllerId + '\'' +
                '}';
    }
}
