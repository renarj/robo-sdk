package com.oberasoftware.robo.dynamixel.protocolv2.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.core.commands.CurrentLimitCommand;
import com.oberasoftware.robo.dynamixel.DynamixelConnector;
import com.oberasoftware.robo.dynamixel.DynamixelInstruction;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2Address;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2ReturnPacket;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

import static com.oberasoftware.robo.core.ConverterUtil.*;
import static com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket.bb2hex;
import static org.slf4j.LoggerFactory.getLogger;

@Component
@ConditionalOnProperty(value = "protocol.v2.enabled", havingValue = "true", matchIfMissing = false)
public class DynamixelV2CurrentLimitHandler implements EventHandler {
    private static final Logger LOG = getLogger(DynamixelV2CurrentLimitHandler.class);

    @Autowired
    private DynamixelConnector connector;

    @EventSubscribe
    public void receive(CurrentLimitCommand command) {
        int servoId = toSafeInt(command.getServoId());
        LOG.debug("Received a servo: {} current limit: {}", command.getServoId(), command);

        DynamixelV2CommandPacket packet = new DynamixelV2CommandPacket(DynamixelInstruction.WRITE_DATA, servoId);

        LOG.debug("Setting Servo: {} current limit: {}", servoId, command.getLimit());
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put(intTo16BitByte(command.getLimit()));

        byte[] data = packet.addParam(DynamixelV2Address.CURRENT_LIMIT, buffer.array()).build();
        LOG.debug("Setting Servo: {} current limit with command: {}", servoId, bb2hex(data));
        byte[] received = connector.sendAndReceive(data);
        DynamixelV2ReturnPacket rp = new DynamixelV2ReturnPacket(received);
        if(rp.hasErrors()) {
            LOG.error("Servo: {} current limit package error: {}", servoId, bb2hex(received));
        }

    }
}
