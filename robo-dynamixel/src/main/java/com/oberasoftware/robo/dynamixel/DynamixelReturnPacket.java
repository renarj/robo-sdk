package com.oberasoftware.robo.dynamixel;

import com.google.common.base.Preconditions;

import java.util.Arrays;

import static com.oberasoftware.robo.dynamixel.DynamixelCommandPacket.bb2hex;

/**
 * @author Renze de Vries
 */
public class DynamixelReturnPacket {
    public static final int PARAM_OFFSET = 5;
    private final byte[] data;

    private final int id;
    private final int length;
    private final int errorCode;

    public DynamixelReturnPacket(byte[] data) {
        Preconditions.checkArgument(data.length >= 6);

        this.data = data;
        this.id = data[2];
        this.length = data[3];
        this.errorCode = data[4];
    }

    public int getId() {
        return this.id;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public boolean hasErrors() {
        return errorCode != 0;
    }

    public byte[] getParameters() {
        if(length > DynamixelCommandPacket.FIXED_PARAM_LENGTH) {
            return Arrays.copyOfRange(data, PARAM_OFFSET, (PARAM_OFFSET + length) -
                    DynamixelCommandPacket.FIXED_PARAM_LENGTH);
        } else {
            return new byte[0];
        }
    }

    @Override
    public String toString() {
        return "DynamixelReturnPacket{" +
                "data=" + bb2hex(data) +
                ", id=" + id +
                ", length=" + length +
                ", params=[" + bb2hex(getParameters()) + "]" +
                '}';
    }
}
