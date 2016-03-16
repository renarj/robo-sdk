package com.oberasoftware.robo.core.sensors;

import com.oberasoftware.robo.api.sensors.SensorConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Renze de Vries
 */
public class AnalogToDistanceConverter implements SensorConverter<Double, Integer> {
    private static final Logger LOG = LoggerFactory.getLogger(AnalogToDistanceConverter.class);

    private final static Double MAX_SCALE = 2.4;


    @Override
    public Integer convert(Double input) {
        LOG.info("Converting voltage: {} to distance");

        return 0;
    }
}
