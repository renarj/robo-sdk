package com.oberasoftware.robo.dynamixel.protocolv2.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.commands.TorgueCommand;
import com.oberasoftware.robo.api.commands.TorgueLimitCommand;
import com.oberasoftware.robo.dynamixel.DynamixelAddress;
import com.oberasoftware.robo.dynamixel.DynamixelConnector;
import com.oberasoftware.robo.dynamixel.DynamixelInstruction;
import com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1CommandPacket;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2Address;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2ReturnPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static com.oberasoftware.robo.core.ConverterUtil.intTo16BitByte;
import static com.oberasoftware.robo.core.ConverterUtil.toSafeInt;
import static com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket.bb2hex;

/**
 * @author Renze de Vries
 */
@Component
@ConditionalOnProperty(value = "protocol.v2.enabled", havingValue = "true", matchIfMissing = false)
public class DynamixelV2TorgueHandler implements EventHandler, DynamixelTorgueHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelV2TorgueHandler.class);

    @Autowired
    private DynamixelConnector connector;

    @Override
    @EventSubscribe
    public void receive(TorgueCommand torgueCommand) {
        LOG.info("Received a torgue command: {}", torgueCommand);
        int servoId = toSafeInt(torgueCommand.getServoId());

        int targetTorgueState = 0x00;
        if(torgueCommand.isEnableTorque()) {
            targetTorgueState = 0x01;
        }

        LOG.debug("Setting torgue to: {} for servo: {}", targetTorgueState, servoId);

        byte[] response = connector.sendAndReceive(new DynamixelV2CommandPacket(DynamixelInstruction.WRITE_DATA, servoId)
                .add8BitParam(DynamixelV2Address.TORGUE_ENABLE, targetTorgueState)
                .build());

        DynamixelV2ReturnPacket packet = new DynamixelV2ReturnPacket(response);
        if(packet.hasErrors()) {
            LOG.error("Could not set torgue response: {} for servo: {}", bb2hex(response), servoId);
        }
        LOG.debug("Received torgue response: {} for servo: {} errors: {} reason: {}", bb2hex(response), servoId, packet.hasErrors(), packet.getErrorReason());
    }

    @Override
    @EventSubscribe
    public void receive(TorgueLimitCommand torgueLimitCommand) {
        LOG.info("Received a torgue limit command: {}", torgueLimitCommand);

        int servoId = toSafeInt(torgueLimitCommand.getServoId());

        connector.sendAndReceive(new DynamixelV1CommandPacket(DynamixelInstruction.WRITE_DATA, servoId)
                .addParam(DynamixelAddress.TORGUE_LIMIT_L, intTo16BitByte(torgueLimitCommand.getTorgueLimit()))
                .build());
    }
}
