package com.oberasoftware.home.core.mqtt.converters;

import com.oberasoftware.robo.api.commands.BasicCommand;
import com.oberasoftware.robo.api.converters.Converter;
import com.oberasoftware.robo.api.converters.TypeConverter;
import com.oberasoftware.home.core.mqtt.MQTTMessage;
import com.oberasoftware.home.core.mqtt.MQTTMessageImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.oberasoftware.home.util.ConverterHelper.mapToJson;

/**
 * @author Renze de Vries
 */
@Component
public class BasicCommandConverter implements Converter {
    private static final Logger LOG = LoggerFactory.getLogger(BasicCommandConverter.class);

    private static final String TOPIC_PATH = "/commands/%s/%s/%s";

    @TypeConverter
    public MQTTMessage convert(BasicCommand basicCommand) {
        String topic = String.format(TOPIC_PATH, basicCommand.getControllerId(), basicCommand.getItemId(), basicCommand.getCommandType());
        LOG.info("Forward basic command: {} to topic: {}", basicCommand, topic);
        return new MQTTMessageImpl(topic, mapToJson(basicCommand));
    }
}
