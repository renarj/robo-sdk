package com.oberasoftware.home.core.mqtt;

import com.oberasoftware.robo.api.model.Value;
import com.oberasoftware.home.util.ConverterHelper;
import com.oberasoftware.robo.core.model.ValueTransportMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Renze de Vries
 */
public class MQTTMessageBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(MQTTMessageBuilder.class);


    private static final String TOPIC_FORMAT = "/%s/%s/%s/%s";

    private String controllerId;
    private String label;
    private String channel;
    private Value value;

    public MQTTMessageBuilder(Value value) {
        this.value = value;
    }

    public static MQTTMessageBuilder from(Value value) {
        return new MQTTMessageBuilder(value);
    }

    public MQTTMessageBuilder controller(String controllerId) {
        this.controllerId = controllerId;
        return this;
    }

    public MQTTMessageBuilder channel(String channel) {
        this.channel = channel;
        return this;
    }

    public MQTTMessageBuilder label(String label) {
        this.label = label;
        return this;
    }

    public MQTTMessage build() {
        String topic = String.format(TOPIC_FORMAT, MessageGroup.STATES.name().toLowerCase(), controllerId,
                channel, label);
        ValueTransportMessage message = new ValueTransportMessage(value, controllerId, channel, label);
        return new MQTTMessageImpl(topic, ConverterHelper.mapToJson(message));
    }
}
