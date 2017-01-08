package com.oberasoftware.robo.cloud.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.api.commands.BasicCommand;
import com.oberasoftware.home.api.model.BasicCommandImpl;
import com.oberasoftware.home.core.mqtt.MQTTMessage;
import com.oberasoftware.home.core.mqtt.MQTTPath;
import com.oberasoftware.home.core.mqtt.MessageGroup;
import com.oberasoftware.home.util.IntUtils;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.RobotRegistry;
import com.oberasoftware.robo.api.servo.ServoDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static com.oberasoftware.home.util.ConverterHelper.mapFromJson;

/**
 * @author Renze de Vries
 */
@Component
public class ServoCommandHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ServoCommandHandler.class);
    private static final int DEFAULT_SPEED = 20;

    @Autowired
    private RobotRegistry robotRegistry;

    @EventSubscribe
    @MQTTPath(group = MessageGroup.COMMANDS, device = "servos", label = "position")
    public void convert(MQTTMessage mqttMessage) {
        LOG.debug("Executing Servo command from topic: {}", mqttMessage.getMessage(), mqttMessage.getTopic());
        BasicCommand basicCommand = mapFromJson(mqttMessage.getMessage(), BasicCommandImpl.class);

        Robot robot = robotRegistry.getRobot(basicCommand.getControllerId());
        ServoDriver servoDriver = robot.getServoDriver();

        String servoPosition = basicCommand.getProperty("position");
        String servoId = basicCommand.getProperty("servoId");
        String speed = basicCommand.getProperty("speed");
        if(StringUtils.hasText(servoPosition) && StringUtils.hasText(servoId)) {
            LOG.info("Setting servo: {} to position: {}", servoId, servoPosition);

            if(StringUtils.hasText(speed)) {
                servoDriver.setPositionAndSpeed(servoId, IntUtils.toInt(speed, DEFAULT_SPEED),
                        IntUtils.toSafeInt(servoPosition));
            } else {
                servoDriver.setTargetPosition(servoId, IntUtils.toSafeInt(servoPosition));
            }
        } else {
            LOG.warn("Received servo command, but no servoId or Position specified");
        }
    }

    @EventSubscribe
    @MQTTPath(group = MessageGroup.COMMANDS, device = "servos", label = "torgue")
    public void torgue(MQTTMessage mqttMessage) {
        LOG.debug("Executing Servo command from topic: {}", mqttMessage.getMessage(), mqttMessage.getTopic());
        BasicCommand basicCommand = mapFromJson(mqttMessage.getMessage(), BasicCommandImpl.class);

        Robot robot = robotRegistry.getRobot(basicCommand.getControllerId());
        ServoDriver servoDriver = robot.getServoDriver();

        String servoId = basicCommand.getProperty("servoId");
        boolean torgueEnabled = Boolean.parseBoolean(basicCommand.getProperty("torgue"));
        Optional<Integer> tl = IntUtils.toInt(basicCommand.getProperty("torgueLimit"));
        if(tl.isPresent()) {
            servoDriver.setTorgue(servoId, tl.get());
        } else {
            servoDriver.setTorgue(servoId, torgueEnabled);
        }
    }
}