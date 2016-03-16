package com.oberasoftware.robo.core.sensors;

import com.oberasoftware.robo.api.sensors.SensorConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Renze de Vries
 */
public class AnalogToDistanceConverter implements SensorConverter<Double, Integer> {
    private static final Logger LOG = LoggerFactory.getLogger(AnalogToDistanceConverter.class);

    //TODO: Hardcoded for now, needs to be fixed
    private final static Double MAX_SCALE_VOLT = 2.33;
    private final static Double MIN_SCALE_VOLT = 0.02;

    private final static Double MAX_DISTANCE = 80.0;
    private final static Double MIN_DISTANCE = 10.0;

    @Override
    public Integer convert(Double input) {
        LOG.info("Converting voltage: {} to distance", input);

        Double oldRange = MAX_SCALE_VOLT - MIN_SCALE_VOLT;
        Double newRange = MAX_DISTANCE - MIN_DISTANCE;

        Double newValue = MAX_DISTANCE - (((input - MIN_SCALE_VOLT) * newRange) / oldRange);

        return newValue.intValue();
    }
}
