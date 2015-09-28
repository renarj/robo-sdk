package com.oberasoftware.robo.api;

import com.oberasoftware.base.event.Event;

/**
 * @author Renze de Vries
 */
public interface ServoCommand extends Event {
    String getServoId();
}
