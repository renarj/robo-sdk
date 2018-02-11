package com.oberasoftware.robo.api.model;

import java.util.Map;

/**
 * @author renarj
 */
public interface Device extends com.oberasoftware.robo.api.model.HomeEntity {
    String getControllerId();

    Map<String, String> getProperties();
}
