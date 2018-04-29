package com.oberasoftware.robo.dynamixel.protocolv1;

import com.oberasoftware.robo.dynamixel.DynamixelReturnPacket;

import java.util.Arrays;
import java.util.BitSet;

import static com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1CommandPacket.bb2hex;


/**
 * @author Renze de Vries
 */
public class DynamixelV1ReturnPacket implements DynamixelReturnPacket {
    public static final int PARAM_OFFSET = 5;
    private final byte[] data;

    private final int id;
    private final int length;
    private final int errorCode;

    public DynamixelV1ReturnPacket(byte[] data) {
        if(data.length >= 6) {
            this.data = data;
            this.id = data[2];
            this.length = data[3];
            this.errorCode = data[4];
        } else {
            this.data = data;
            this.length = 0;
            if(this.data.length == 5) {
                this.errorCode = data[4];
            } else {
                this.errorCode = -1;
            }
            this.id = 0;
        }
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorReason() {
        return getErrorReason(errorCode);
    }

    private static String getErrorReason(int errorCode) {
        if(errorCode > -1) {
            StringBuilder builder = new StringBuilder();
            BitSet s = BitSet.valueOf(new byte[]{(byte) errorCode});
            if (s.get(6)) {
                builder.append("Instruction error; ");
            }
            if (s.get(5)) {
                builder.append("Overload error; ");
            }
            if (s.get(4)) {
                builder.append("Checksum error; ");
            }
            if (s.get(3)) {
                builder.append("Range error; ");
            }
            if (s.get(2)) {
                builder.append("Overheating error; ");
            }
            if (s.get(1)) {
                builder.append("Angle limit error; ");
            }
            if (s.get(0)) {
                builder.append("input voltage error; ");
            }

            return builder.toString();
        } else {
            return "uknown error";
        }
    }

    @Override
    public boolean hasErrors() {
        return errorCode != 0;
    }

    @Override
    public byte[] getParameters() {
        if(length > DynamixelV1CommandPacket.FIXED_PARAM_LENGTH) {
            return Arrays.copyOfRange(data, PARAM_OFFSET, (PARAM_OFFSET + length) -
                    DynamixelV1CommandPacket.FIXED_PARAM_LENGTH);
        } else {
            return new byte[0];
        }
    }

    @Override
    public String toString() {
        return "DynamixelV1ReturnPacket{" +
                "data=" + bb2hex(data) +
                ", id=" + id +
                ", length=" + length +
                ", params=[" + bb2hex(getParameters()) + "]" +
                '}';
    }
}
