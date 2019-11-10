package com.oberasoftware.robo.dynamixel.protocolv1.handlers;

import com.google.common.collect.ImmutableMap;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.servo.ServoProperty;
import com.oberasoftware.robo.api.servo.events.ServoDataReceivedEvent;
import com.oberasoftware.robo.core.ServoDataImpl;
import com.oberasoftware.robo.core.commands.ReadPositionAndSpeedCommand;
import com.oberasoftware.robo.dynamixel.*;
import com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1ReturnPacket;
import com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1CommandPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.oberasoftware.robo.core.ConverterUtil.byteToInt;
import static com.oberasoftware.robo.core.ConverterUtil.toSafeInt;
import static com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1CommandPacket.bb2hex;
import static java.lang.String.valueOf;

/**
 * @author Renze de Vries
 */
@Component
@ConditionalOnProperty(value = "protocol.v2.enabled", havingValue = "false", matchIfMissing = true)
public class DynamixelV1ReadPositionHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelV1ReadPositionHandler.class);

    @Autowired
    private DynamixelConnector connector;

    @EventSubscribe
    public ServoDataReceivedEvent receive(ReadPositionAndSpeedCommand command) {
        int servoId = toSafeInt(command.getServoId());
        LOG.debug("Sending Read command for speed and position for servo: {}", servoId);

        byte[] data = new DynamixelV1CommandPacket(DynamixelInstruction.READ_DATA, servoId)
                .addParam(DynamixelAddress.PRESENT_POSITION_L, 0x04)
                .build();
        byte[] received = connector.sendAndReceive(data);

        DynamixelV1ReturnPacket returnPacket = new DynamixelV1ReturnPacket(received);
        if(!returnPacket.hasErrors()) {
            LOG.trace("Received a speed and position reply: {} for servo: {}", bb2hex(received), servoId);

            byte[] params = returnPacket.getParameters();
            if(params.length == 4) {
                int position = byteToInt(params[0], params[1]);
                int speed = byteToInt(params[2], params[3]);

                LOG.debug("Servo: {} has position: {} and speed: {}", servoId, position, speed);

                Map<ServoProperty, Object> map = new ImmutableMap.Builder<ServoProperty, Object>()
                        .put(ServoProperty.POSITION, position)
                        .put(ServoProperty.SPEED, speed)
                        .build();

                return new ServoDataReceivedEvent(valueOf(servoId), new ServoDataImpl(command.getServoId(), map));
            } else {
                LOG.warn("Incorrect number of parameters in return package was: {}", bb2hex(params));
            }
        } else {
            LOG.error("Received an error: {} for speed and position for servo: {}", returnPacket.getErrorReason(), servoId);
        }

        return null;
    }
}
