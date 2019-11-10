package com.oberasoftware.robo.dynamixel.protocolv2.handlers;

import com.google.common.collect.ImmutableMap;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.servo.ServoProperty;
import com.oberasoftware.robo.api.servo.events.ServoDataReceivedEvent;
import com.oberasoftware.robo.core.ServoDataImpl;
import com.oberasoftware.robo.core.commands.ReadAngleLimit;
import com.oberasoftware.robo.dynamixel.DynamixelConnector;
import com.oberasoftware.robo.dynamixel.DynamixelInstruction;
import com.oberasoftware.robo.core.commands.AngleLimitCommand;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2Address;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2ReturnPacket;
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
@ConditionalOnProperty(value = "protocol.v2.enabled", havingValue = "true", matchIfMissing = false)
public class DynamixelV2AngleLimitHandler implements EventHandler {
    private static final Logger LOG = getLogger(DynamixelV2AngleLimitHandler.class);

    @Autowired
    private DynamixelConnector connector;

    @EventSubscribe
    public void receive(AngleLimitCommand command) {
        int servoId = toSafeInt(command.getServoId());
        LOG.debug("Received a servo mode: {}", command.getServoId(), command);

        DynamixelV2CommandPacket packet = new DynamixelV2CommandPacket(DynamixelInstruction.WRITE_DATA, servoId);
        packet.addParam(DynamixelV2Address.MAX_POSITION, intTo32BitByte((command.getMaxLimit()), command.getMinLimit()));

        byte[] data = packet.build();
        byte[] received = connector.sendAndReceive(data);
        LOG.info("Package has been delivered");

        DynamixelV2ReturnPacket returnPacket = new DynamixelV2ReturnPacket(received);
        if (!returnPacket.hasErrors()) {
            LOG.debug("Mode for servo: {} set to: {}", servoId, command);
        } else {
            LOG.error("Could not set servo: {} to mode: {} reason: {}", servoId, command, returnPacket.getErrorReason());
        }
    }

    @EventSubscribe
    public ServoDataReceivedEvent receive(ReadAngleLimit command) {
        int servoId = toSafeInt(command.getServoId());
        LOG.info("Received a read operation for the servo mode: {}", command.getServoId());

        DynamixelV2CommandPacket packet = new DynamixelV2CommandPacket(DynamixelInstruction.READ_DATA, servoId);
        byte[] data = packet.addInt16Bit(DynamixelV2Address.MAX_POSITION, (byte)0x08).build();
        byte[] received = connector.sendAndReceive(data);

        DynamixelV2ReturnPacket returnPacket = new DynamixelV2ReturnPacket(received);
        if(!returnPacket.hasErrors()) {
            LOG.info("Received an angle limit readout: {} for servo: {}", bb2hex(received), servoId);

            byte[] params = returnPacket.getParameters();
            if(params.length == 8) {
                int maxLimit = byteToInt32(params[0], params[1], params[2], params[3]);
                int minLimit = byteToInt32(params[4], params[5], params[6], params[7]);

                LOG.info("Servo: {} has minimum angle limit: {} and maximum: {}", servoId, minLimit, maxLimit);

                Map<ServoProperty, Object> map = new ImmutableMap.Builder<ServoProperty, Object>()
                        .put(ServoProperty.MIN_ANGLE_LIMIT, minLimit)
                        .put(ServoProperty.MAX_ANGLE_LIMIT, maxLimit)
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
