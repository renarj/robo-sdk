package com.oberasoftware.home.client.api;

/**
 * @author Renze de Vries
 */
public interface ClientResponse {

    enum RESPONSE_STATUS {
        OK,
        FAILED
    }


    RESPONSE_STATUS getStatus();
}
