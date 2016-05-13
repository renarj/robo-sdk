package com.oberasoftware.robo.cloud.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.core.mqtt.MQTTMessage;
import com.oberasoftware.home.core.mqtt.MQTTPath;
import com.oberasoftware.home.core.mqtt.MessageGroup;
import com.oberasoftware.robo.api.MotionEngine;
import com.oberasoftware.robo.api.motion.WalkDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Renze de Vries
 */
@Component
public class WalkMotionCommandHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(WalkMotionCommandHandler.class);

    @Autowired
    private MotionEngine motionEngine;

    @EventSubscribe
    @MQTTPath(group = MessageGroup.COMMANDS, device = "motion", label = "walk")
    public void convert(MQTTMessage mqttMessage) {
        LOG.debug("Executing Walk: {} from topic: {}", mqttMessage.getMessage(), mqttMessage.getTopic());
        WalkDirection walkDirection = WalkDirection.fromString(mqttMessage.getMessage());
        LOG.info("Walking in direction: {}", walkDirection);

        if(walkDirection != WalkDirection.STOP) {
            motionEngine.walk(walkDirection);
        } else {
            motionEngine.stopWalking();
        }
    }
}