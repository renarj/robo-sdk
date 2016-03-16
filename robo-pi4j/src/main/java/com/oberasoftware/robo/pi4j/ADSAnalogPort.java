package com.oberasoftware.robo.pi4j;

import com.oberasoftware.robo.api.sensors.AnalogPort;
import com.oberasoftware.robo.api.sensors.PortListener;
import com.oberasoftware.robo.api.sensors.VoltageValue;
import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Renze de Vries
 */
public class ADSAnalogPort implements AnalogPort {
    private List<PortListener<VoltageValue>> listeners = new CopyOnWriteArrayList<>();

    protected ADSAnalogPort(ADS1115GpioProvider provider, GpioPinAnalogInput input) {
        GpioPinListenerAnalog listener = event -> {
            double value = event.getValue();
            double percent =  ((value * 100) / ADS1115GpioProvider.ADS1115_RANGE_MAX_VALUE);
            double voltage = provider.getProgrammableGainAmplifier(event.getPin()).getVoltage() * (percent/100);

            listeners.forEach(l -> l.receive((VoltageValue) () -> voltage));
        };
        input.addListener(listener);
    }

    @Override
    public void listen(PortListener<VoltageValue> portListener) {
        listeners.add(portListener);
    }
}
