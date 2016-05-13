package com.oberasoftware.robo.cloud.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.core.mqtt.MQTTMessage;
import com.oberasoftware.home.core.mqtt.MQTTPath;
import com.oberasoftware.home.core.mqtt.MessageGroup;
import com.oberasoftware.robo.api.MotionEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Renze de Vries
 */
@Component
public class RunMotionCommandHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RunMotionCommandHandler.class);

    @Autowired
    private MotionEngine motionEngine;

    @EventSubscribe
    @MQTTPath(group = MessageGroup.COMMANDS, device = "motion", label = "run")
    public void convert(MQTTMessage mqttMessage) {
        LOG.info("Executing motion: {} from topic: {}", mqttMessage.getMessage(), mqttMessage.getTopic());
        String motionCommand = mqttMessage.getMessage();

        motionEngine.runMotion(motionCommand);
    }
}
