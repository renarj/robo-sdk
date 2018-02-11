package com.oberasoftware.home.core.mqtt;

/**
 * @author Renze de Vries
 */
public class MQTTMessageImpl implements MQTTMessage {

    private final String topic;
    private final String message;

    public MQTTMessageImpl(String topic, String message) {
        this.topic = topic;
        this.message = message;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MQTTMessageImpl{" +
                "topic='" + topic + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
