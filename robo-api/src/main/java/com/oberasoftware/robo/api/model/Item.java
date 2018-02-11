package com.oberasoftware.robo.api.model;

import java.util.Map;

/**
 * @author renarj
 */
public interface Item extends com.oberasoftware.robo.api.model.HomeEntity {
    Map<String, String> getProperties();
}
