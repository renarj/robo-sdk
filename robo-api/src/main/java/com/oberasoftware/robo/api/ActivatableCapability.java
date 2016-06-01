package com.oberasoftware.robo.api;

import java.util.Map;

/**
 * @author Renze de Vries
 */
public interface ActivatableCapability extends Capability {
    void activate(Robot robot, Map<String, String> properties);

    void shutdown();
}
