package com.oberasoftware.home.core.mqtt;

/**
 * @author Renze de Vries
 */
public interface MQTTListener {
    void receive(String topic, String payload);
}
