package com.oberasoftware.robo.dynamixel;

import java.util.List;

public interface DynamixelCommandPacket {
    DynamixelCommandPacket addParam(DynamixelAddress address, int param);

    DynamixelCommandPacket addParam(DynamixelAddress address, byte... params);

    DynamixelCommandPacket addParam(DynamixelAddress address, List<Byte> params);

    byte[] build();
}
