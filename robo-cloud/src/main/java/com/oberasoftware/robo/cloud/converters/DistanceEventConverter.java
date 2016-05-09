package com.oberasoftware.robo.cloud.converters;

import com.oberasoftware.home.api.converters.Converter;
import com.oberasoftware.home.api.converters.TypeConverter;
import com.oberasoftware.home.api.impl.types.ValueImpl;
import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.core.mqtt.MQTTMessage;
import com.oberasoftware.home.core.mqtt.MQTTMessageImpl;
import com.oberasoftware.home.core.mqtt.MessageGroup;
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

    private static final String TOPIC_PATH = "/%s/%s/%s/%s";

    @TypeConverter
    public MQTTMessage convert(DistanceSensorEvent event) {
        LOG.info("Converting event: {} to mqtt", event);

        String topic = String.format(TOPIC_PATH, MessageGroup.STATES.name(),
                event.getRobotName(), event.getCapability(), event.getSourceName());
        Value value = new ValueImpl(VALUE_TYPE.NUMBER, event.getDistance());

        LOG.info("Sending value: {} to topic: {}", value, topic);
        return new MQTTMessageImpl(topic, value.asString());
    }
}
