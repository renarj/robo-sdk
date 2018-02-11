package com.oberasoftware.robo.api.exceptions;

/**
 * @author Renze de Vries
 */
public class SecurityException extends RuntimeHomeAutomationException {
    public SecurityException(String message, Throwable e) {
        super(message, e);
    }

    public SecurityException(String message) {
        super(message);
    }
}
