package com.oberasoftware.robo.api.sensors;

/**
 * @author Renze de Vries
 */
public interface DirectPort<Y extends SensorValue> extends Port {
    void listen(PortListener<Y> portListener);
}
