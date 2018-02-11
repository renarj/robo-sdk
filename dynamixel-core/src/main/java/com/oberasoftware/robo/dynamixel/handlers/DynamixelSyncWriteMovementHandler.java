package com.oberasoftware.robo.dynamixel.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.commands.BulkPositionSpeedCommand;
import com.oberasoftware.robo.api.commands.PositionAndSpeedCommand;
import com.oberasoftware.robo.dynamixel.DynamixelAddress;
import com.oberasoftware.robo.dynamixel.DynamixelCommandPacket;
import com.oberasoftware.robo.dynamixel.DynamixelConnector;
import com.oberasoftware.robo.dynamixel.DynamixelInstruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DynamixelSyncWriteMovementHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelSyncWriteMovementHandler.class);

    @Autowired
    private DynamixelConnector connector;

    @EventSubscribe
    public void receive(BulkPositionSpeedCommand command) {
        LOG.debug("Received a bulk command: {}", command);

        Map<String, PositionAndSpeedCommand> commands = command.getCommands();
        DynamixelCommandPacket packet = new DynamixelCommandPacket(DynamixelInstruction.SYNC_WRITE,
                DynamixelCommandPacket.BROADCAST_ID);

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
