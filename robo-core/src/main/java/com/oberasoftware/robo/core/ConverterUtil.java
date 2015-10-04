package com.oberasoftware.robo.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Renze de Vries
 */
public class ConverterUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ConverterUtil.class);

    public static int toSafeInt(String id) {
        try {
            return Integer.parseInt(id);
        } catch(NumberFormatException e) {
            LOG.debug("Invalid integer: " + id + ", returning -1");
            return -1;
        }
    }

    /**
     * This is in essence a big endian to little endian conversion maxing at 16 bits
     * @param number The java integer
     * @return The byte array in LE
     */
    public static byte[] intTo16BitByte(int number) {
        return new byte[] {
                (byte) (number & 0xff), //first the lower bits
                (byte) (number >> 8) //second is the higher bits
        };
    }

    public static byte[] intTo16BitByte(int... numbers) {
        byte[] data = new byte[numbers.length * 2];
        for(int i=0; i<numbers.length; i++) {
            data[i * 2] = (byte)(numbers[i] & 0xff);
            data[(i * 2) + 1] = (byte)(numbers[i] >> 8);
        }
        return data;
    }

    public static int byteToInt(byte lowByte, byte highByte) {
        return ((highByte << 8) + (lowByte & 0xff));
    }
}
