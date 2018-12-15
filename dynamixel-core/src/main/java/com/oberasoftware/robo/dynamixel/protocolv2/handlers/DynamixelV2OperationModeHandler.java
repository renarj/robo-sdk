package com.oberasoftware.robo.dynamixel.protocolv2.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.core.commands.OperationModeCommand;
import com.oberasoftware.robo.dynamixel.DynamixelConnector;
import com.oberasoftware.robo.dynamixel.DynamixelInstruction;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2Address;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2ReturnPacket;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static com.oberasoftware.robo.core.ConverterUtil.intTo16BitByte;
import static com.oberasoftware.robo.core.ConverterUtil.toSafeInt;
import static com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket.bb2hex;
import static org.slf4j.LoggerFactory.getLogger;

@Component
@ConditionalOnProperty(value = "protocol.v2.enabled", havingValue = "true", matchIfMissing = false)
public class DynamixelV2OperationModeHandler implements EventHandler {
    private static final Logger LOG = getLogger(DynamixelV2OperationModeHandler.class);


    @Autowired
    private DynamixelConnector connector;

    @EventSubscribe
    public void receive(OperationModeCommand command) {
        int servoId = toSafeInt(command.getServoId());
        LOG.debug("Received a servo operation mode command: {}", command.getServoId(), command);

        int dynamixelMode;
        switch(command.getOperationMode()) {
            case VELOCITY_MODE:
                dynamixelMode = 1;
                break;
            case EXTENDED_POSITION_CONTROL:
                dynamixelMode = 4;
                break;
            default:
            case POSITION_CONTROL:
                dynamixelMode = 3;
                break;
        }

        DynamixelV2CommandPacket packet = new DynamixelV2CommandPacket(DynamixelInstruction.WRITE_DATA, servoId);
        packet.addParam(DynamixelV2Address.OPERATING_MODE, intTo16BitByte(dynamixelMode));

        byte[] data = packet.build();
        byte[] received = connector.sendAndReceive(data);
        LOG.info("Operating mode package has been delivered");

        DynamixelV2ReturnPacket returnPacket = new DynamixelV2ReturnPacket(received);
        if (!returnPacket.hasErrors()) {
            LOG.debug("Mode for servo: {} set to: {}", servoId, command);
        } else {
            LOG.info("Packet data: {}", bb2hex(received));
            LOG.error("Could not set servo: {} to operating mode: {} reason: {}", servoId, command, returnPacket.getErrorReason());
        }
    }

}
