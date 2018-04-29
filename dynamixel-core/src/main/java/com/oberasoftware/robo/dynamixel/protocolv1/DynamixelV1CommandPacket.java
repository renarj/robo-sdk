package com.oberasoftware.robo.dynamixel.protocolv1;

import com.oberasoftware.robo.dynamixel.DynamixelAddress;
import com.oberasoftware.robo.dynamixel.DynamixelCommandPacket;
import com.oberasoftware.robo.dynamixel.DynamixelInstruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Renze de Vries
 */
public class DynamixelV1CommandPacket implements DynamixelCommandPacket {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelV1CommandPacket.class);

    private static final int FIXED_MESSAGE_LENGTH = 6;
    public static final int FIXED_PARAM_LENGTH = 2;
    public static final int BROADCAST_ID = 0xFE;

    private DynamixelInstruction dynamixelInstruction;
    private int id;
    private List<Byte> parameters = new ArrayList<>();

    public DynamixelV1CommandPacket(DynamixelInstruction dynamixelInstruction, int id) {
        this.dynamixelInstruction = dynamixelInstruction;
        this.id = id;
    }

    @Override
    public DynamixelCommandPacket addParam(DynamixelAddress address, int param) {
        return addParam(address, new byte[]{(byte)param});
    }

    @Override
    public DynamixelCommandPacket addParam(DynamixelAddress address, byte... params) {
        this.parameters.add((byte)address.getAddress());
        for(byte b : params) {
            this.parameters.add(b);
        }

        return this;
    }

    @Override
    public DynamixelCommandPacket addParam(DynamixelAddress address, List<Byte> params) {
        this.parameters.add((byte)address.getAddress());
        this.parameters.addAll(params);
        return this;
    }

    @Override
    public byte[] build() {
        ByteBuffer buffer = ByteBuffer.allocate(FIXED_MESSAGE_LENGTH + parameters.size());
        buffer.put((byte)0xff);
        buffer.put((byte)0xff);
        buffer.put((byte)id);
        int length = FIXED_PARAM_LENGTH + parameters.size();
        buffer.put((byte)length);
        buffer.put((byte) dynamixelInstruction.getInstruction());
        parameters.forEach(buffer::put);
        buffer.put((byte) calculateChecksum());

        return buffer.array();
    }

    public int calculateChecksum() {
        int length = FIXED_PARAM_LENGTH + parameters.size();

        int total = id + length + dynamixelInstruction.getInstruction();
        total += parameters.stream().mapToInt(Byte::intValue).sum();

        int checksum = (byte) (255 - ((total) % 256));

        return checksum;
    }

    public static String bb2hex(byte[] buffer) {
        String result = "";
        for (byte b : buffer) {
            result = result + String.format("%02X ", b);
        }
        return result;
    }

    public static String bb2hex(List<Byte> buffer) {
        return buffer.stream().map(b -> String.format("%02X", b))
                .collect(Collectors.joining(" "));
    }

    @Override
    public String toString() {
        return "DynamixelCommandPacket{" +
                "dynamixelInstruction=" + dynamixelInstruction +
                ", id=" + id +
                ", parameters=" + bb2hex(parameters) +
                '}';
    }
}
