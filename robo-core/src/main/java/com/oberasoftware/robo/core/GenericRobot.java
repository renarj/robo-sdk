package com.oberasoftware.robo.core;

import com.oberasoftware.base.event.Event;
import com.oberasoftware.base.event.EventBus;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.robo.api.*;
import com.oberasoftware.robo.api.servo.ServoDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Renze de Vries
 */
public class GenericRobot implements Robot {
    private static final Logger LOG = LoggerFactory.getLogger(GenericRobot.class);

    private EventBus eventBus;

    private final String robotName;
    private final boolean isRemote;

    private final List<CapabilityHolder> capabilities;
    private final List<SensorHolder> sensors;

    public GenericRobot(String robotName, boolean isRemote,
                        EventBus eventBus, List<CapabilityHolder> capabilities, List<SensorHolder> sensors) {
        this.robotName = robotName;
        this.eventBus = eventBus;
        this.isRemote = isRemote;

        this.capabilities = capabilities;
        this.sensors = sensors;
    }

    public void initialize() {
        this.capabilities.forEach(c -> c.initializeCapability(this));
        this.sensors.forEach(s -> s.initializeSensor(this));
    }

    @Override
    public String getName() {
        return robotName;
    }

    @Override
    public void listen(EventHandler robotEventHandler) {
        eventBus.registerHandler(robotEventHandler);
    }

    @Override
    public void publish(Event robotEvent) {
        eventBus.publish(robotEvent);
    }

    @Override
    public ServoDriver getServoDriver() {
        return getCapability(ServoDriver.class);
    }

    @Override
    public MotionEngine getMotionEngine() {
        return getCapability(MotionEngine.class);
    }

    @Override
    public <T extends Capability> T getCapability(Class<T> capabilityClass) {
        Optional<Capability> o = capabilities.stream()
                .map(CapabilityHolder::getCapability)
                .filter(capabilityClass::isInstance)
                .findFirst();
        if(o.isPresent()) {
            return (T)o.get();
        } else {
            return null;
        }
    }

    @Override
    public RemoteDriver getRemoteDriver() {
        return getCapability(RemoteDriver.class);
    }

    @Override
    public List<Capability> getCapabilities() {
        return capabilities.stream().map(CapabilityHolder::getCapability).collect(Collectors.toList());
    }

    @Override
    public boolean isRemote() {
        return isRemote;
    }

    @Override
    public void shutdown() {
        LOG.info("Shutting down robot: {}", robotName);
        capabilities.forEach(c -> {
            if(c.getCapability() instanceof ActivatableCapability) {
                ((ActivatableCapability)c.getCapability()).shutdown();
            }
        });
        LOG.info("Robot: {} shutdown complete", robotName);
    }
}
