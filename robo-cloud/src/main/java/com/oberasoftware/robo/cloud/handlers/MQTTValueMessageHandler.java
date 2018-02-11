package com.oberasoftware.robo.cloud.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.core.model.ValueTransportMessage;
import com.oberasoftware.home.core.mqtt.MQTTMessage;
import com.oberasoftware.home.core.mqtt.MQTTPath;
import com.oberasoftware.home.core.mqtt.MessageGroup;
import com.oberasoftware.home.util.ConverterHelper;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.RobotRegistry;
import com.oberasoftware.robo.api.events.ValueEventImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Renze de Vries
 */
@Component
public class MQTTValueMessageHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(MQTTValueMessageHandler.class);

    @Autowired
    private RobotRegistry robotRegistry;

    @EventSubscribe
    @MQTTPath(group = MessageGroup.STATES)
    public void convert(MQTTMessage mqttMessage) {
        ValueTransportMessage message = ConverterHelper.mapFromJson(mqttMessage.getMessage(), ValueTransportMessage.class);
        LOG.info("Received sensor value information: {}", message);

        Robot robot = robotRegistry.getRobot(message.getControllerId());
        robot.publish(new ValueEventImpl(message.getControllerId(), message.getChannelId(), message.getLabel(), message.getValue()));
    }

    public static void main(String[] args) {
        String j = "{\"value\":{\"value\":520,\"type\":\"NUMBER\"},\"controllerId\":\"max\",\"channelId\":\"Hand\",\"label\":\"5\"}";
        ValueTransportMessage t = ConverterHelper.mapFromJson(j, ValueTransportMessage.class);
        t.getValue();

    }
}
