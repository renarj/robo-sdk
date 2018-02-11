package com.oberasoftware.robo.cloud.handlers.motion;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.commands.BasicCommand;
import com.oberasoftware.robo.core.model.BasicCommandImpl;
import com.oberasoftware.home.core.mqtt.MQTTMessage;
import com.oberasoftware.home.core.mqtt.MQTTPath;
import com.oberasoftware.home.core.mqtt.MessageGroup;
import com.oberasoftware.robo.api.MotionEngine;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.RobotRegistry;
import com.oberasoftware.robo.api.motion.controller.HandsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.oberasoftware.home.util.ConverterHelper.mapFromJson;

/**
 * @author Renze de Vries
 */
@Component
public class RemoteHandsHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RemoteHandsHandler.class);

    @Autowired
    private RobotRegistry robotRegistry;

    @EventSubscribe
    @MQTTPath(group = MessageGroup.COMMANDS, device = "motion", label = "hands")
    public void convert(MQTTMessage mqttMessage) {
        LOG.info("Executing motion: {} from topic: {}", mqttMessage.getMessage(), mqttMessage.getTopic());
        BasicCommand basicCommand = mapFromJson(mqttMessage.getMessage(), BasicCommandImpl.class);

        String handsPosition = basicCommand.getProperty("position");
        String hand = basicCommand.getProperty("hands");
        if(handsPosition != null) {
            LOG.info("Received hand motion execution: {} for hand: {}", handsPosition, hand);

            Robot robot = robotRegistry.getRobot(basicCommand.getControllerId());
            MotionEngine motionEngine = robot.getMotionEngine();
            Optional<HandsController> handsController = motionEngine.getMotionController(HandsController.CONTROLLER_NAME);
            if(handsController.isPresent()) {
                HandsController.HAND_ID handId = getHand(hand);
                if(handsPosition.equalsIgnoreCase("open")) {
                    LOG.info("Opening hand for hand: {}", handId);
                    handsController.get().openHand(handId);
                } else {
                    LOG.info("Closing hand for hand: {}", handId);
                    handsController.get().closeHand(handId);
                }
            }

        } else {
            LOG.warn("Received hand command, but not position specified");
        }
    }

    private HandsController.HAND_ID getHand(String hand) {
        if(hand != null) {
            if(hand.equalsIgnoreCase(HandsController.HAND_ID.LEFT.name())) {
                return HandsController.HAND_ID.LEFT;
            } else if(hand.equalsIgnoreCase(HandsController.HAND_ID.RIGHT.name())) {
                return HandsController.HAND_ID.RIGHT;
            }
        }

        return HandsController.HAND_ID.ALL;
    }

}
