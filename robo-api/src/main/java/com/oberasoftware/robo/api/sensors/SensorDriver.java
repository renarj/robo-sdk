package com.oberasoftware.robo.api.sensors;

import com.oberasoftware.robo.api.ActivatableCapability;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface SensorDriver<T extends Port> extends ActivatableCapability {

    List<T> getPorts();

    T getPort(String portId);
}
