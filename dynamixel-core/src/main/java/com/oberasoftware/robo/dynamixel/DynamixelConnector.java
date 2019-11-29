package com.oberasoftware.robo.dynamixel;

/**
 * @author Renze de Vries
 */
public interface DynamixelConnector {
    void connect(String serialPort);

    void disconnect();

    byte[] sendAndReceive(byte[] bytes);

    void sendNoReceive(byte[] bytes);
}
