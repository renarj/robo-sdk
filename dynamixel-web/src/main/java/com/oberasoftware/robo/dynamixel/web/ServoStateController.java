package com.oberasoftware.robo.dynamixel.web;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.commands.Scale;
import com.oberasoftware.robo.api.servo.ServoData;
import com.oberasoftware.robo.api.servo.ServoProperty;
import com.oberasoftware.robo.api.servo.events.ServoUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * @author Renze de Vries
 */
@Controller
public class ServoStateController implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ServoStateController.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @EventSubscribe
    public void receiveStateUpdate(ServoUpdateEvent stateUpdateEvent) {
        LOG.debug("Received servo update event: {}", stateUpdateEvent);
        ServoData data = stateUpdateEvent.getServoData();
        Integer tValue = data.getValue(ServoProperty.TEMPERATURE);
        Double cValue = data.getValue(ServoProperty.VOLTAGE);

        Integer speed = data.getValue(ServoProperty.SPEED);
        Scale speedScale = data.getValue(ServoProperty.SPEED_SCALE);
        Integer position = data.getValue(ServoProperty.POSITION);
        Scale positionScale = data.getValue(ServoProperty.POSITION_SCALE);

        int temp = tValue != null ? tValue : 0;
        double voltage = cValue != null ? cValue : 0.0;

        SimpleServo servo = new SimpleServo(stateUpdateEvent.getServoId(),
                speed != null ? speedScale.convertToScale(speed, new Scale(-100, 100)) : 0,
                position != null ? positionScale.convertToScale(position, new Scale(0, 2000)) : 0
                , 0,
                temp,
                voltage);
        messagingTemplate.convertAndSend("/topic/state", servo);
    }
}
