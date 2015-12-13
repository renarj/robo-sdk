package com.oberasoftware.robo.dynamixel.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.dynamixel.DynamixelAddress;
import com.oberasoftware.robo.dynamixel.DynamixelCommandPacket;
import com.oberasoftware.robo.dynamixel.DynamixelConnector;
import com.oberasoftware.robo.dynamixel.DynamixelInstruction;
import com.oberasoftware.robo.core.commands.BulkPositionSpeedCommand;
import com.oberasoftware.robo.core.commands.PositionAndSpeedCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.oberasoftware.robo.dynamixel.DynamixelCommandPacket.bb2hex;
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

        List<Byte> servoBytes = commands.values().stream().map(this::getServoBytes)
                .flatMap(Collection::stream).collect(Collectors.toList());
        servoBytes.add(0, (byte)0x04); //add param length

        packet.addParam(DynamixelAddress.GOAL_POSITION_L, servoBytes);
        byte[] data = packet.build();
        LOG.debug("This would be the package: {}", bb2hex(data));

        connector.sendAndReceive(data);
    }

    private List<Byte> getServoBytes(PositionAndSpeedCommand command) {
        int servoId = toSafeInt(command.getServoId());

        List<Byte> servoBytes = new ArrayList<>();
        servoBytes.add((byte)servoId);
        byte[] bytes = intTo16BitByte(command.getPosition(), command.getSpeed());
        for(byte b : bytes) {
            servoBytes.add(b);
        }
        return servoBytes;
    }
}
