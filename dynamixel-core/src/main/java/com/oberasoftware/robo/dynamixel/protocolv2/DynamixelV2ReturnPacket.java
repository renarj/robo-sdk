package com.oberasoftware.robo.dynamixel.protocolv2;

import com.oberasoftware.robo.core.ConverterUtil;
import com.oberasoftware.robo.dynamixel.DynamixelReturnPacket;
import com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1CommandPacket;

import java.util.Arrays;
import java.util.BitSet;

import static com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket.bb2hex;

/**
 * @author Renze de Vries
 */
public class DynamixelV2ReturnPacket implements DynamixelReturnPacket {
    private static final int FIXED_PARAM_START_OFFSET = 9;
    public static final int PACKET_WINDOW_SIZE_INFO = 4;

    private final byte[] data;

    private final int id;
    private final int length;
    private final int errorCode;

    public DynamixelV2ReturnPacket(byte[] data) {
        if(data.length >= 9) {
            this.data = data;
            this.id = data[4];
            this.length = ConverterUtil.byteToInt(data[5], data[6]);
            this.errorCode = data[8];
        } else {
            this.data = data;
            this.length = 0;
//            if(this.data.length == 5) {
//                this.errorCode = data[4];
//            } else {
                this.errorCode = -1;
//            }
            this.id = 0;
        }
    }

    public int getId() {
        return this.id;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorReason() {
        return getErrorReason(errorCode);
    }

    public static String getErrorReason(int errorCode) {
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

    public boolean hasErrors() {
        return errorCode != 0;
    }

    public byte[] getParameters() {
//        if(length > FIXED_PARAM_START_OFFSET) {
        return Arrays.copyOfRange(data, FIXED_PARAM_START_OFFSET, (FIXED_PARAM_START_OFFSET + (length - PACKET_WINDOW_SIZE_INFO)));
//        } else {
//            return new byte[0];
//        }
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
