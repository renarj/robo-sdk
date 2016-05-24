package com.oberasoftware.robo.cloud.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.api.commands.BasicCommand;
import com.oberasoftware.home.api.model.BasicCommandImpl;
import com.oberasoftware.home.core.mqtt.MQTTMessage;
import com.oberasoftware.home.core.mqtt.MQTTPath;
import com.oberasoftware.home.core.mqtt.MessageGroup;
import com.oberasoftware.robo.api.MotionEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Renze de Vries
 */
@Component
public class RunMotionCommandHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RunMotionCommandHandler.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.enableDefaultTyping();
    }

    @Autowired
    private MotionEngine motionEngine;

    @EventSubscribe
    @MQTTPath(group = MessageGroup.COMMANDS, device = "motion", label = "run")
    public void convert(MQTTMessage mqttMessage) {
        LOG.info("Executing motion: {} from topic: {}", mqttMessage.getMessage(), mqttMessage.getTopic());
        try {
            BasicCommand basicCommand = OBJECT_MAPPER.readValue(mqttMessage.getMessage(), BasicCommandImpl.class);

            String motionName = basicCommand.getProperties().get("motion");
            if(motionName != null) {
                LOG.info("Received motion execution: {}", motionName);
                motionEngine.runMotion(motionName);
            } else {
                LOG.warn("Received motion command, but motion not specified");
            }
        } catch (IOException e) {
            LOG.error("Could not load basic command", e);
        }
    }
}
