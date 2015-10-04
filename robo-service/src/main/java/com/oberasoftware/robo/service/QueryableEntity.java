package com.oberasoftware.robo.service;

/**
 * @author Renze de Vries
 */
public interface QueryableEntity {
    String getId();

    String getProperty(String name);
}
