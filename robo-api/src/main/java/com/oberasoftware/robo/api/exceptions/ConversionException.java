package com.oberasoftware.robo.api.exceptions;

/**
 * @author Renze de Vries
 */
public class ConversionException extends RuntimeHomeAutomationException {
    public ConversionException(String message, Throwable e) {
        super(message, e);
    }

    public ConversionException(String message) {
        super(message);
    }
}
