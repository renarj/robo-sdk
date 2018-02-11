package com.oberasoftware.robo.core.events.controller;

import com.oberasoftware.base.event.Event;
import com.oberasoftware.robo.api.model.Device;

/**
 * @author renarj
 */
public class DeviceUpdateEvent implements Event {

    private final String pluginId;
    private final Device device;

    public DeviceUpdateEvent(String pluginId, Device device) {
        this.pluginId = pluginId;
        this.device = device;
    }

    public String getPluginId() {
        return pluginId;
    }

    public Device getDevice() {
        return device;
    }

    @Override
    public String toString() {
        return "DeviceInformationEvent{" +
                "device=" + device +
                ", pluginId='" + pluginId + '\'' +
                '}';
    }
}
