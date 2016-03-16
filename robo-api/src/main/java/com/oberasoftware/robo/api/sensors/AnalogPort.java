package com.oberasoftware.robo.api.sensors;

/**
 * @author Renze de Vries
 */
public interface AnalogPort extends Port {
    void listen(PortListener<VoltageValue> portListener);
}
