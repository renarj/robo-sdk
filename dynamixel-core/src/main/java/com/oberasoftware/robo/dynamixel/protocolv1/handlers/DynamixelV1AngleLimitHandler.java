package com.oberasoftware.robo.dynamixel.protocolv1.handlers;

import com.google.common.collect.ImmutableMap;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.servo.ServoProperty;
import com.oberasoftware.robo.api.servo.events.ServoDataReceivedEvent;
import com.oberasoftware.robo.core.ServoDataImpl;
import com.oberasoftware.robo.core.commands.AngleLimitCommand;
import com.oberasoftware.robo.core.commands.ReadAngleLimit;
import com.oberasoftware.robo.dynamixel.*;
import com.oberasoftware.robo.dynamixel.DynamixelCommandPacket;
import com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1ReturnPacket;
import com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1CommandPacket;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.oberasoftware.robo.core.ConverterUtil.*;
import static com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1CommandPacket.bb2hex;
import static java.lang.String.valueOf;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
@ConditionalOnProperty(value = "protocol.v2.enabled", havingValue = "false", matchIfMissing = true)
public class DynamixelV1AngleLimitHandler implements EventHandler {
    private static final Logger LOG = getLogger(DynamixelV1AngleLimitHandler.class);

    @Autowired
    private DynamixelConnector connector;

    @EventSubscribe
    public void receive(AngleLimitCommand command) {
        int servoId = toSafeInt(command.getServoId());
        LOG.debug("Received a servo mode: {}", command.getServoId(), command);

        DynamixelCommandPacket packet = new DynamixelV1CommandPacket(DynamixelInstruction.WRITE_DATA, servoId);
        packet.addParam(DynamixelAddress.CW_ANGLE_LIMIT_L, intTo16BitByte(command.getMinLimit(), command.getMaxLimit()));

        byte[] data = packet.build();
        byte[] received = connector.sendAndReceive(data);
        LOG.info("Package has been delivered");

        DynamixelV1ReturnPacket returnPacket = new DynamixelV1ReturnPacket(received);
        if (!returnPacket.hasErrors()) {
            LOG.debug("Mode for servo: {} set to: {}", servoId, command);
        } else {
            LOG.error("Could not set servo: {} to mode: {} reason: {}", servoId, command, returnPacket.getErrorReason());
        }
    }

    @EventSubscribe
    public ServoDataReceivedEvent receive(ReadAngleLimit command) {
        int servoId = toSafeInt(command.getServoId());
        LOG.debug("Received a read operation for the servo mode: {}", command.getServoId());

        DynamixelCommandPacket packet = new DynamixelV1CommandPacket(DynamixelInstruction.READ_DATA, servoId);
        byte[] data = packet.addParam(DynamixelAddress.CW_ANGLE_LIMIT_L, 0x04).build();
        byte[] received = connector.sendAndReceive(data);

        DynamixelV1ReturnPacket returnPacket = new DynamixelV1ReturnPacket(received);
        if(!returnPacket.hasErrors()) {
            LOG.trace("Received a mode readout: {} for servo: {}", bb2hex(received), servoId);

            byte[] params = returnPacket.getParameters();
            if(params.length == 4) {
                int minAngleLimit = byteToInt(params[0], params[1]);
                int maxAngleLimit = byteToInt(params[2], params[3]);

                LOG.debug("Servo: {} has minigmum angle limit: {} and maximum: {}", servoId, minAngleLimit, maxAngleLimit);

                Map<ServoProperty, Object> map = new ImmutableMap.Builder<ServoProperty, Object>()
                        .put(ServoProperty.MIN_ANGLE_LIMIT, minAngleLimit)
                        .put(ServoProperty.MAX_ANGLE_LIMIT, maxAngleLimit)
                        .build();

                return new ServoDataReceivedEvent(valueOf(servoId), new ServoDataImpl(command.getServoId(), map));
            } else {
                LOG.warn("Incorrect number of parameters in return package was: {}", bb2hex(params));
            }
        } else {
            LOG.error("Received an error: {} for mode readout for servo: {}", returnPacket.getErrorReason(), servoId);
        }
        return null;
    }
}
