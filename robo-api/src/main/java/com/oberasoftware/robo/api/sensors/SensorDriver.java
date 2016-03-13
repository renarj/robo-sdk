package com.oberasoftware.robo.api.sensors;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface SensorDriver<T extends Port> {
    List<T> getPorts();

    T getPort(String portId);
}
