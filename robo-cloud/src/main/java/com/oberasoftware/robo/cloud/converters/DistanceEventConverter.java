package com.oberasoftware.robo.cloud.converters;

import com.oberasoftware.robo.api.converters.Converter;
import com.oberasoftware.robo.api.converters.TypeConverter;
import com.oberasoftware.robo.api.model.VALUE_TYPE;
import com.oberasoftware.robo.api.model.Value;
import com.oberasoftware.robo.core.model.ValueImpl;
import com.oberasoftware.home.core.mqtt.MQTTMessage;
import com.oberasoftware.home.core.mqtt.MQTTMessageBuilder;
import com.oberasoftware.robo.api.events.DistanceSensorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Renze de Vries
 */
@Component
public class DistanceEventConverter implements Converter {
    private static final Logger LOG = LoggerFactory.getLogger(DistanceEventConverter.class);

    @TypeConverter
    public MQTTMessage convert(DistanceSensorEvent event) {
        LOG.info("Converting event: {} to mqtt", event);

        Value value = new ValueImpl(VALUE_TYPE.NUMBER, event.getDistance());

        MQTTMessage message = MQTTMessageBuilder.from(value)
                .controller(event.getControllerId())
                .channel(event.getItemId())
                .label(event.getLabel())
            .build();

        LOG.info("Sending value: {} MQTT message: {}", value, message);
        return message;
    }
}
