package com.oberasoftware.robo.cloud.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.core.mqtt.MQTTMessage;
import com.oberasoftware.home.core.mqtt.MQTTPath;
import com.oberasoftware.home.core.mqtt.MessageGroup;
import com.oberasoftware.robo.api.behavioural.BehaviouralRobot;
import com.oberasoftware.robo.api.behavioural.BehaviouralRobotRegistry;
import com.oberasoftware.robo.api.commands.BasicCommand;
import com.oberasoftware.robo.api.exceptions.RuntimeHomeAutomationException;
import com.oberasoftware.robo.api.navigation.DirectionalInput;
import com.oberasoftware.robo.api.navigation.RobotNavigationController;
import com.oberasoftware.robo.core.model.BasicCommandImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.oberasoftware.home.util.ConverterHelper.mapFromJson;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class RemoteNavigationHandler implements EventHandler {
    private static final Logger LOG = getLogger(RemoteNavigationHandler.class);

    private final BehaviouralRobotRegistry robotRegistry;

    @Autowired
    public RemoteNavigationHandler(BehaviouralRobotRegistry robotRegistry) {
        this.robotRegistry = robotRegistry;
    }

    @EventSubscribe
    @MQTTPath(group = MessageGroup.COMMANDS, device = "navigation", label = "input")
    public void convert(MQTTMessage mqttMessage) {
        LOG.info("Executing navigation command: {} from topic: {}", mqttMessage.getMessage(), mqttMessage.getTopic());
        BasicCommand basicCommand = mapFromJson(mqttMessage.getMessage(), BasicCommandImpl.class);

        Map<String, Double> directionInput = new HashMap<>();
        basicCommand.getProperties().forEach((k, v) -> {
            try {
                Double parsedValue = Double.parseDouble(v);
                directionInput.put(k, parsedValue);
            } catch(NumberFormatException e) {
                LOG.warn("Received direction input with invalid value: " + v);
            }
        });

        Optional<BehaviouralRobot> behaviouralRobot = robotRegistry.getRobot(basicCommand.getControllerId());
        behaviouralRobot.ifPresent((b) -> {
            RobotNavigationController navigationController = b.getBehaviour(RobotNavigationController.class);
            LOG.info("Sending directional input: {} to robot: {}", directionInput, basicCommand.getControllerId());
            navigationController.move(new DirectionalInput(directionInput));
        });
        behaviouralRobot.orElseThrow(() -> new RuntimeHomeAutomationException("Could not find robot with id: " + basicCommand.getControllerId()));
    }
}
