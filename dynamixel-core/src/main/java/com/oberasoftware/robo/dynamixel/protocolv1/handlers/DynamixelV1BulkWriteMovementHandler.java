package com.oberasoftware.robo.dynamixel.protocolv1.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.commands.BulkPositionSpeedCommand;
import com.oberasoftware.robo.api.commands.PositionAndSpeedCommand;
import com.oberasoftware.robo.dynamixel.*;
import com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1CommandPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.oberasoftware.robo.core.ConverterUtil.intTo16BitByte;
import static com.oberasoftware.robo.core.ConverterUtil.toSafeInt;

/**
 * @author Renze de Vries
 */
@Component
@ConditionalOnProperty(value = "protocol.v2.enabled", havingValue = "false", matchIfMissing = true)
public class DynamixelV1BulkWriteMovementHandler implements EventHandler, BulkWriteMovementHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelV1BulkWriteMovementHandler.class);

    @Autowired
    private DynamixelConnector connector;

    @EventSubscribe
    @Override
    public void receive(BulkPositionSpeedCommand command) {
        LOG.debug("Received a bulk command: {}", command);

        Map<String, PositionAndSpeedCommand> commands = command.getCommands();
        DynamixelCommandPacket packet = new DynamixelV1CommandPacket(DynamixelInstruction.SYNC_WRITE,
                DynamixelV1CommandPacket.BROADCAST_ID);

        List<Byte> servoBytes = new ArrayList<>();
        //we pass the list by reference to write results in to prevent losing time on slower hardware (raspberry, etc.)
        commands.forEach((k, v) -> getServoBytes(servoBytes, v));

        servoBytes.add(0, (byte)0x04); //add param length
        packet.addParam(DynamixelAddress.GOAL_POSITION_L, servoBytes);
        byte[] data = packet.build();

        connector.sendAndReceive(data);
    }

    private void getServoBytes(List<Byte> servoBytes, PositionAndSpeedCommand command) {
        int servoId = toSafeInt(command.getServoId());

        servoBytes.add((byte)servoId);
        byte[] bytes = intTo16BitByte(command.getPosition(), command.getSpeed());
        for(byte b : bytes) {
            servoBytes.add(b);
        }
    }
}
