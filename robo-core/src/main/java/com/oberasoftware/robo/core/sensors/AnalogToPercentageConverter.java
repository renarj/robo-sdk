package com.oberasoftware.robo.core.sensors;

import com.oberasoftware.robo.api.sensors.SensorConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Renze de Vries
 */
public class AnalogToPercentageConverter implements SensorConverter<Double, Double> {
    private static final Logger LOG = LoggerFactory.getLogger(AnalogToPercentageConverter.class);

    @Override
    public Double convert(Double input) {
        LOG.info("Converting voltage: {} to percentage", input);
        return 0.0;
    }
}
