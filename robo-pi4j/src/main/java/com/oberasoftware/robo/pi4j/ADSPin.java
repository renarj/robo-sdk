package com.oberasoftware.robo.pi4j;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.impl.PinImpl;

import java.util.EnumSet;

/**
 * @author Renze de Vries
 */
public class ADSPin {

    public static final Pin INPUT_A0 = createAnalogInputPin(0, "ANALOG INPUT 0");
    public static final Pin INPUT_A1 = createAnalogInputPin(1, "ANALOG INPUT 1");
    public static final Pin INPUT_A2 = createAnalogInputPin(2, "ANALOG INPUT 2");
    public static final Pin INPUT_A3 = createAnalogInputPin(3, "ANALOG INPUT 3");

    public static Pin[] ALL = { ADSPin.INPUT_A0, ADSPin.INPUT_A1, ADSPin.INPUT_A2, ADSPin.INPUT_A3 };

    private static Pin createAnalogInputPin(int address, String name) {
        return new PinImpl(ADSGPIOProvider.NAME, address, name, EnumSet.of(PinMode.ANALOG_INPUT));
    }
}
