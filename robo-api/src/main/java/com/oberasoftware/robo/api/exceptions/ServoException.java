package com.oberasoftware.robo.api.exceptions;

/**
 * @author Renze de Vries
 */
public class ServoException extends RoboException {
    public ServoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServoException(String message) {
        super(message);
    }
}
