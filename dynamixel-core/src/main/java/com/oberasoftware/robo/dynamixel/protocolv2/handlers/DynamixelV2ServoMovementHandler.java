package com.oberasoftware.robo.dynamixel.protocolv2.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.commands.PositionAndSpeedCommand;
import com.oberasoftware.robo.api.commands.PositionCommand;
import com.oberasoftware.robo.api.commands.SpeedCommand;
import com.oberasoftware.robo.api.servo.ServoCommand;
import com.oberasoftware.robo.dynamixel.DynamixelConnector;
import com.oberasoftware.robo.dynamixel.DynamixelInstruction;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2Address;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2ReturnPacket;
import org.apache.tomcat.util.buf.ByteBufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

import static com.oberasoftware.robo.core.ConverterUtil.intTo16BitByte;
import static com.oberasoftware.robo.core.ConverterUtil.intTo32BitByte;
import static com.oberasoftware.robo.core.ConverterUtil.toSafeInt;
import static com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket.bb2hex;

/**
 * @author Renze de Vries
 */
@Component
@ConditionalOnProperty(value = "protocol.v2.enabled", havingValue = "true", matchIfMissing = false)
public class DynamixelV2ServoMovementHandler implements EventHandler, DynamixelServoMovementHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelV2ServoMovementHandler.class);

    private static final int NOT_SPECIFIED = -1;

    @Autowired
    private DynamixelConnector connector;

    @Override
    @EventSubscribe
    public void receive(PositionCommand positionCommand) {
        LOG.debug("Received servo position command: {}", positionCommand);

        setGoal(positionCommand, positionCommand.getPosition());
    }

    @Override
    @EventSubscribe
    public void receive(SpeedCommand speedCommand) {
        LOG.debug("Received a speed command: {}", speedCommand);

        setSpeed(speedCommand, speedCommand.getSpeed());
    }

    @Override
    @EventSubscribe
    public void receive(PositionAndSpeedCommand command) {
        LOG.debug("Received a position and speed command: {}", command);

        setGoal(command, command.getPosition());
        setSpeed(command, command.getSpeed());
    }


    private void setGoal(ServoCommand command, int goal) {
        int servoId = toSafeInt(command.getServoId());
        DynamixelV2CommandPacket packet = new DynamixelV2CommandPacket(DynamixelInstruction.WRITE_DATA, servoId);
        if(goal > NOT_SPECIFIED) {
            LOG.debug("Setting Servo: {} goal to: {}", servoId, goal);
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.put(intTo32BitByte(goal));

            packet.addParam(DynamixelV2Address.GOAL_POSITION_L, buffer.array());

            byte[] dataToSend = packet.build();
            LOG.debug("Sending movement command: {}", bb2hex(dataToSend));

            byte[] received = connector.sendAndReceive(dataToSend);
            DynamixelV2ReturnPacket returnPacket = new DynamixelV2ReturnPacket(received);
            LOG.debug("Received return package: {} errors detected: {} reason: {}",
                    returnPacket, returnPacket.hasErrors(), returnPacket.getErrorReason());
        }
    }

    private void setSpeed(ServoCommand command, int speed) {
        int servoId = toSafeInt(command.getServoId());
        DynamixelV2CommandPacket packet = new DynamixelV2CommandPacket(DynamixelInstruction.WRITE_DATA, servoId);
        if(speed > NOT_SPECIFIED) {
            LOG.debug("Setting Servo: {} speed: {}", servoId, speed);
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.put(intTo32BitByte(speed));

            packet.addParam(DynamixelV2Address.GOAL_VELOCITY, buffer.array());

            byte[] dataToSend = packet.build();
            LOG.info("Sending movement command: {}", bb2hex(dataToSend));

            LOG.debug("Sending package: {}", packet);
            byte[] received = connector.sendAndReceive(dataToSend);
            DynamixelV2ReturnPacket returnPacket = new DynamixelV2ReturnPacket(received);
            LOG.debug("Received return package: {} errors detected: {} reason: {}",
                    returnPacket, returnPacket.hasErrors(), returnPacket.getErrorReason());

        }

    }
}
