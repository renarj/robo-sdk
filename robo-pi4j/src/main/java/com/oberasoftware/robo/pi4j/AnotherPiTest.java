package com.oberasoftware.robo.pi4j;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.i2c.I2CBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author Renze de Vries
 */
@EnableAutoConfiguration
@Configuration
@ComponentScan
public class AnotherPiTest {
    private static final Logger LOG = LoggerFactory.getLogger(AnotherPiTest.class);

    public static void main(String[] args) {
        final GpioController gpio = GpioFactory.getInstance();

        try {
            final ADSGPIOProvider gpioProvider = new ADSGPIOProvider(I2CBus.BUS_1, 0x68);
            // provision gpio analog input pins from ADS1015
            final GpioPinAnalogInput distanceSensorPin = gpio.provisionAnalogInputPin(gpioProvider, ADSPin.INPUT_A0, "DistanceSensor-A0");

            gpioProvider.setProgrammableGainAmplifier(ExtBaseGpioProvider.ProgrammableGainAmplifierValue.PGA_4_096V, ADSPin.ALL);


            // Define a threshold value for each pin for analog value change events to be raised.
            // It is important to set this threshold high enough so that you don't overwhelm your program with change events for insignificant changes
            gpioProvider.setEventThreshold(150, ADSPin.ALL);

            gpioProvider.setMonitorInterval(100);

            for(int i=0; i<50; i++) {
                LOG.info("Reading");

                double voltage = gpioProvider.getProgrammableGainAmplifier(distanceSensorPin).getVoltage();
                LOG.info("Voltage: {]", voltage);

                Thread.sleep(1000);
            }

        } catch (IOException | InterruptedException e) {
            LOG.error("", e);
        }
    }
}
