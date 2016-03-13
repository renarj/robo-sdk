package com.oberasoftware.robo.pi4j;

import com.oberasoftware.base.event.EventBus;
import com.oberasoftware.robo.api.sensors.AnalogPort;
import com.oberasoftware.robo.api.sensors.SensorDriver;
import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1115Pin;
import com.pi4j.gpio.extension.ads.ADS1x15GpioProvider;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.i2c.I2CBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

/**
 * @author Renze de Vries
 */
@Component
public class ADS1115Driver implements SensorDriver<AnalogPort> {
    private static final Logger LOG = LoggerFactory.getLogger(ADS1115Driver.class);

    @Autowired
    private EventBus eventBus;

    @PostConstruct
    public void init() throws IOException {
        final GpioController gpio = GpioFactory.getInstance();
        final ADS1115GpioProvider gpioProvider = new ADS1115GpioProvider(I2CBus.BUS_1, ADS1115GpioProvider.ADS1115_ADDRESS_0x48);
        GpioPinAnalogInput myInputs[] = {
                gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A0, "MyAnalogInput-A0"),
                gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A1, "MyAnalogInput-A1"),
                gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A2, "MyAnalogInput-A2"),
                gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A3, "MyAnalogInput-A3"),
        };
        gpioProvider.setProgrammableGainAmplifier(ADS1x15GpioProvider.ProgrammableGainAmplifierValue.PGA_4_096V, ADS1115Pin.ALL);
        gpioProvider.setEventThreshold(500, ADS1115Pin.ALL);
        gpioProvider.setMonitorInterval(100);

    }

    @Override
    public List<AnalogPort> getPorts() {
        return null;
    }

    @Override
    public AnalogPort getPort(String portId) {
        return null;
    }
}
