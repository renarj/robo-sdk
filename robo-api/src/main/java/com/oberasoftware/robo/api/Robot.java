package com.oberasoftware.robo.api;

import com.oberasoftware.base.event.Event;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.robo.api.servo.ServoDriver;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface Robot {
    String getName();

    boolean isRemote();

    void listen(EventHandler robotEventHandler);

    void publish(Event robotEvent);

    MotionEngine getMotionEngine();

    ServoDriver getServoDriver();

    RemoteDriver getRemoteDriver();

    List<Capability> getCapabilities();

    <T extends Capability> T getCapability(Class<T> capabilityClass);

    void shutdown();
}
