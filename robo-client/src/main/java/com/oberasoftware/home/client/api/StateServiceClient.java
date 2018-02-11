package com.oberasoftware.home.client.api;

/**
 * @author Renze de Vries
 */
public interface StateServiceClient {
    void connect();

    void disconnect();

    void listen(StateServiceListener listener);
}
