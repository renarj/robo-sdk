package com.oberasoftware.robo.dynamixel;

/**
 * @author Renze de Vries
 */
public interface DynamixelConnector {
    void connect();

    void disconnect();

    byte[] sendAndReceive(byte[] bytes);
}
