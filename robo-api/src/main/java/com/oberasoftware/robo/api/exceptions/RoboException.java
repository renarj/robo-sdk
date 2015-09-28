package com.oberasoftware.robo.api.exceptions;

/**
 * @author Renze de Vries
 */
public class RoboException extends RuntimeException {
    public RoboException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoboException(String message) {
        super(message);
    }
}
