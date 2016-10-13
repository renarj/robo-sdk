package com.oberasoftware.robo.cloud.converters;

import com.oberasoftware.home.api.converters.Converter;
import com.oberasoftware.home.api.converters.TypeConverter;
import com.oberasoftware.home.api.model.ValueImpl;
import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.core.mqtt.MQTTMessage;
import com.oberasoftware.home.core.mqtt.MQTTMessageBuilder;
import com.oberasoftware.robo.api.events.BumperEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BumperEventConverter implements Converter {
    private static final Logger LOG = LoggerFactory.getLogger(BumperEventConverter.class);

    @TypeConverter
    public MQTTMessage convert(BumperEvent event) {
        LOG.info("Converting event: {} to mqtt", event);

        Value value = new ValueImpl(VALUE_TYPE.BOOLEAN, event.isTriggered());

        MQTTMessage message = MQTTMessageBuilder.from(value)
                .controller(event.getControllerId())
                .channel(event.getItemId())
                .label(event.getLabel())
                .build();

        LOG.info("Sending value: {} MQTT message: {}", value, message);
        return message;
    }
}
