package com.oberasoftware.robo.dynamixel;

import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2Address;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket;
import org.junit.Test;

import java.nio.ByteBuffer;

import static com.oberasoftware.robo.core.ConverterUtil.intTo32BitByte;
import static com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket.bb2hex;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DynamixelV2PacketTest {
    @Test
    public void testRegWritePackage() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(intTo32BitByte(200));

        byte[] data = new DynamixelV2CommandPacket(DynamixelInstruction.REG_WRITE, 0x01)
                .addParam(DynamixelV2Address.GOAL_VELOCITY, buffer.array())
                .build();

        assertThat(bb2hex(data), is("FF FF FD 00 01 09 00 04 68 00 C8 00 00 00 AE 8E"));
    }

    @Test
    public void testActionPackage() {
        byte[] data = new DynamixelV2CommandPacket(DynamixelInstruction.ACTION, 0x01)
                .build();

        assertThat(bb2hex(data), is("FF FF FD 00 01 03 00 05 02 CE"));
    }
}
