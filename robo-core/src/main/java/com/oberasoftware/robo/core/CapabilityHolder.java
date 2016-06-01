package com.oberasoftware.robo.core;

import com.oberasoftware.robo.api.ActivatableCapability;
import com.oberasoftware.robo.api.Capability;
import com.oberasoftware.robo.api.Robot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Renze de Vries
 */
public class CapabilityHolder {
    private static final Logger LOG = LoggerFactory.getLogger(CapabilityHolder.class);

    private Capability capability;
    private Map<String, String> properties;

    public CapabilityHolder(Capability capability, Map<String, String> properties) {
        this.capability = capability;
        this.properties = properties;
    }

    public Capability getCapability() {
        return capability;
    }

    public void initializeCapability(Robot robot) {
        if (capability instanceof ActivatableCapability) {
            LOG.info("Activating capability: {} with properties: {}", capability, properties);
            ((ActivatableCapability) capability).activate(robot, properties);
        }
    }
}
