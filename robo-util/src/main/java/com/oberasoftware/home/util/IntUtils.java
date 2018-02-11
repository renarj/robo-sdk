package com.oberasoftware.home.util;

import java.util.Optional;

/**
 * @author Renze de Vries
 */
public class IntUtils {
    public static int toInt(String number, int defaultValue) {
        Optional<Integer> n = toInt(number);
        if(n.isPresent()) {
            return n.get();
        } else {
            return defaultValue;
        }
    }

    public static Optional<Integer> toInt(String number) {
        return Optional.ofNullable(toSafeInt(number));
    }

    public static Integer toSafeInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch(NumberFormatException e) {
            return null;
        }
    }
}
