package com.oberasoftware.robo.dynamixel.handlers;

import com.google.common.collect.ImmutableMap;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.ServoProperty;
import com.oberasoftware.robo.api.ServoUpdateEvent;
import com.oberasoftware.robo.core.ServoDataImpl;
import com.oberasoftware.robo.core.ServoUpdateEventImpl;
import com.oberasoftware.robo.core.commands.ReadPositionAndSpeedCommand;
import com.oberasoftware.robo.dynamixel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.oberasoftware.robo.dynamixel.DynamixelCommandPacket.bb2hex;
import static com.oberasoftware.robo.core.ConverterUtil.byteToInt;
import static com.oberasoftware.robo.core.ConverterUtil.toSafeInt;
import static java.lang.String.valueOf;

/**
 * @author Renze de Vries
 */
@Component
public class DynamixelReadPositionHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelReadPositionHandler.class);

    @Autowired
    private DynamixelConnector connector;

    @EventSubscribe
    public ServoUpdateEvent receive(ReadPositionAndSpeedCommand command) {
        int servoId = toSafeInt(command.getServoId());
        LOG.debug("Sending Read command for speed and position for servo: {}", servoId);

        byte[] data = new DynamixelCommandPacket(DynamixelInstruction.READ_DATA, servoId)
                .addParam(DynamixelAddress.PRESENT_POSITION_L, 0x04)
                .build();
        byte[] received = connector.sendAndReceive(data);

        DynamixelReturnPacket returnPacket = new DynamixelReturnPacket(received);
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

                return new ServoUpdateEventImpl(valueOf(servoId), new ServoDataImpl(map));
            } else {
                LOG.warn("Incorrect number of parameters in return package was: {}", bb2hex(params));
            }
        }

        LOG.warn("Did not receive a reply for speed and position for servo: {}", servoId);
        return null;
    }
}
