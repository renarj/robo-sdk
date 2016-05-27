package com.oberasoftware.robo.api;

/**
 * @author Renze de Vries
 */
public interface SpeechEngine extends ActivatableCapability {
    void say(String text, String language);
}
