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
import com.oberasoftware.robo.api.motion.WalkDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Renze de Vries
 */
@Component
public class WalkMotionCommandHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(WalkMotionCommandHandler.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.enableDefaultTyping();
    }

    @Autowired
    private MotionEngine motionEngine;

    @EventSubscribe
    @MQTTPath(group = MessageGroup.COMMANDS, device = "motion", label = "walk")
    public void convert(MQTTMessage mqttMessage) {
        LOG.debug("Executing Walk: {} from topic: {}", mqttMessage.getMessage(), mqttMessage.getTopic());

        try {
            BasicCommand basicCommand = OBJECT_MAPPER.readValue(mqttMessage.getMessage(), BasicCommandImpl.class);

            String walkDirection = basicCommand.getProperties().get("direction");
            if(walkDirection != null) {
                WalkDirection direction = WalkDirection.fromString(mqttMessage.getMessage());
                LOG.info("Walking in direction: {}", direction);

                if(direction != WalkDirection.STOP) {
                    motionEngine.walk(direction);
                } else {
                    motionEngine.stopWalking();
                }
            } else {
                LOG.warn("Received walk command, but direction not specified");
            }
        } catch (IOException e) {
            LOG.error("Could not load basic command", e);
        }

    }
}
