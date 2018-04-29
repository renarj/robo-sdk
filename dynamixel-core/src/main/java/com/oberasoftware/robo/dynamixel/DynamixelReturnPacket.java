package com.oberasoftware.robo.dynamixel;

import java.util.BitSet;

public interface DynamixelReturnPacket {
    int getId();

    int getErrorCode();

    String getErrorReason();

    boolean hasErrors();

    byte[] getParameters();
}
