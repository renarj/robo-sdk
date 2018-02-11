package com.oberasoftware.robo.api.messaging;

/**
 * @author Renze de Vries
 */
public interface TopicSender<T> {
    void connect();

    void close();

    void publish(T message);
}
