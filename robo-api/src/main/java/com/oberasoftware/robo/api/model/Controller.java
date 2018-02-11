package com.oberasoftware.robo.api.model;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface Controller extends com.oberasoftware.robo.api.model.HomeEntity {
    String getControllerId();

    List<Device> getDevices();
}
