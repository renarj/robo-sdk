package com.oberasoftware.robo.api.messaging;

/**
 * @author Renze de Vries
 */
public interface TopicListener<T> {
    void connect();

    void close();

    void register(com.oberasoftware.robo.api.messaging.TopicConsumer<T> topicConsumer);
}
