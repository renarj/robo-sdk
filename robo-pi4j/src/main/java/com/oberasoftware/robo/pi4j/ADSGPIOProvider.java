package com.oberasoftware.robo.pi4j;

import com.pi4j.io.i2c.I2CBus;

import java.io.IOException;

/**
 * @author Renze de Vries
 */
public class ADSGPIOProvider extends ExtBaseGpioProvider {

    public static final String NAME = "com.pi4j.gpio.extension.ads.ADS1015GpioProvider";
    public static final String DESCRIPTION = "ADS1015 GPIO Provider";

    protected static final int ADS1015_MAX_IO_PINS = 4;

    // =======================================================================
    // ADS1015 I2C ADDRESS
    // =======================================================================
    public static final int ADS1015_ADDRESS_0x48 = 0x48; // ADDRESS 1 : 0x48 (1001000) ADR -> GND
    public static final int ADS1015_ADDRESS_0x49 = 0x49; // ADDRESS 2 : 0x49 (1001001) ADR -> VDD
    public static final int ADS1015_ADDRESS_0x4A = 0x4A; // ADDRESS 3 : 0x4A (1001010) ADR -> SDA
    public static final int ADS1015_ADDRESS_0x4B = 0x4B; // ADDRESS 4 : 0x4B (1001011) ADR -> SCL

    // =======================================================================
    // ADS1015 VALUE RANGES
    // =======================================================================
    public static final int ADS1015_RANGE_MAX_VALUE =  2047; //0x7FF (12 bits)
    public static final int ADS1015_RANGE_MIN_VALUE = -2048; //0x800 (12 bits)

    // =======================================================================
    // CONVERSION DELAY (in mS)
    // =======================================================================
    protected static final int ADS1015_CONVERSIONDELAY       = 0x01;


    // default constructor
    public ADSGPIOProvider(int busNumber, int address) throws IOException {
        // call super constructor in abstract class
        super(busNumber, address);

        // define specific chip configuration properties
        this.allPins = ADSPin.ALL;
        this.conversionDelay = ADS1015_CONVERSIONDELAY;
        this.bitShift = 4; // Shift 12-bit results right 4 bits for the ADS1015
    }

    public ADSGPIOProvider(I2CBus bus, int address) throws IOException {
        // call super constructor in abstract class
        super(bus, address);

        // define specific chip configuration properties
        this.allPins = ADSPin.ALL;
        this.conversionDelay = ADS1015_CONVERSIONDELAY;
        this.bitShift = 4; // Shift 12-bit results right 4 bits for the ADS1015
    }

    @Override
    public String getName() {
        return NAME;
    }
}
