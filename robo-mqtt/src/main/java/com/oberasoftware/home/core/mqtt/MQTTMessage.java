package com.oberasoftware.home.core.mqtt;

import com.oberasoftware.base.event.Event;

/**
 * @author Renze de Vries
 */
public interface MQTTMessage extends Event {
    String getTopic();

    String getMessage();
}
