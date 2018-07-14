package com.oberasoftware.robo.dynamixel.protocolv2.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.commands.TorgueCommand;
import com.oberasoftware.robo.api.commands.WriteIDCommand;
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

import static com.oberasoftware.robo.core.ConverterUtil.toSafeInt;
import static com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket.bb2hex;

@Component
@ConditionalOnProperty(value = "protocol.v2.enabled", havingValue = "true", matchIfMissing = false)
public class DynamixelV2WriteIDHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelV2WriteIDHandler.class);

    @Autowired
    private DynamixelConnector connector;

    @EventSubscribe
    public void receive(WriteIDCommand writeIDCommand) {
        LOG.info("Received a write ID command: {}", writeIDCommand);
        int oldServoId = toSafeInt(writeIDCommand.getOldId());
        int newServoId = toSafeInt(writeIDCommand.getNewId());


        DynamixelV2CommandPacket packet = new DynamixelV2CommandPacket(DynamixelInstruction.WRITE_DATA, oldServoId);
        byte[] data = packet.add8BitParam(DynamixelV2Address.ID, newServoId).build();
        LOG.info("Sending Write ID (old: {} new: {}) package: {}", oldServoId, newServoId, bb2hex(data));

        DynamixelV2ReturnPacket returnPacket = new DynamixelV2ReturnPacket(connector.sendAndReceive(data));
        LOG.info("Return package: {}", returnPacket);
    }
}
