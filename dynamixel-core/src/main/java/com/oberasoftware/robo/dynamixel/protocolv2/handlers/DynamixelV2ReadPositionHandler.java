package com.oberasoftware.robo.dynamixel.protocolv2.handlers;

import com.google.common.collect.ImmutableMap;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.commands.Scale;
import com.oberasoftware.robo.api.servo.ServoProperty;
import com.oberasoftware.robo.api.servo.events.ServoDataReceivedEvent;
import com.oberasoftware.robo.core.ServoDataImpl;
import com.oberasoftware.robo.core.commands.ReadPositionAndSpeedCommand;
import com.oberasoftware.robo.dynamixel.DynamixelConnector;
import com.oberasoftware.robo.dynamixel.DynamixelInstruction;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2Address;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2ReturnPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.oberasoftware.robo.core.ConverterUtil.byteToInt32;
import static com.oberasoftware.robo.core.ConverterUtil.toSafeInt;
import static com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket.bb2hex;
import static java.lang.String.valueOf;

/**
 * @author Renze de Vries
 */
@Component
@ConditionalOnProperty(value = "protocol.v2.enabled", havingValue = "true", matchIfMissing = false)
public class DynamixelV2ReadPositionHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelV2ReadPositionHandler.class);

    @Autowired
    private DynamixelConnector connector;

    @EventSubscribe
    public ServoDataReceivedEvent receive(ReadPositionAndSpeedCommand command) {
        int servoId = toSafeInt(command.getServoId());
        LOG.debug("Sending Read command for speed and position for servo: {}", servoId);

        byte[] data = new DynamixelV2CommandPacket(DynamixelInstruction.READ_DATA, servoId)
                .addInt16Bit(DynamixelV2Address.PRESENT_SPEED_L, 0x08)
                .build();
        byte[] received = connector.sendAndReceive(data);
        LOG.debug("Received a speed and position reply: {} for servo: {}", bb2hex(received), servoId);

        DynamixelV2ReturnPacket returnPacket = new DynamixelV2ReturnPacket(received);
        if(!returnPacket.hasErrors()) {


            byte[] params = returnPacket.getParameters();
            if(params.length == 8) {
                int speed = byteToInt32(params[0], params[1], params[2], params[3]);
                int position = byteToInt32(params[4], params[5], params[6], params[7]);

                LOG.debug("Servo: {} has position: {} and speed: {}", servoId, position, speed);

                Map<ServoProperty, Object> map = new ImmutableMap.Builder<ServoProperty, Object>()
                        .put(ServoProperty.POSITION_SCALE, new Scale(0, 4095))
                        .put(ServoProperty.POSITION, position)
                        .put(ServoProperty.SPEED_SCALE, new Scale(-400, 400))
                        .put(ServoProperty.SPEED, speed)
                        .build();

                return new ServoDataReceivedEvent(valueOf(servoId), new ServoDataImpl(map));
            } else {
                LOG.warn("Incorrect number of parameters in return package was: {}", bb2hex(params));
            }
        } else {
            LOG.error("Received an error: {} for speed and position for servo: {} data: {}", returnPacket.getErrorReason(), servoId, bb2hex(received));

            byte[] de = new DynamixelV2CommandPacket(DynamixelInstruction.READ_DATA, servoId)
                    .addInt16Bit(DynamixelV2Address.SHUTDOWN, 0x01)
                    .build();
            byte[] re = connector.sendAndReceive(de);
            LOG.error("Received Servo: {} shutdown readout: {}", servoId, bb2hex(re));

        }

        return null;
    }
}
