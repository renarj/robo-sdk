package com.oberasoftware.robo.dynamixel.protocolv2.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.commands.PositionAndSpeedCommand;
import com.oberasoftware.robo.api.commands.PositionCommand;
import com.oberasoftware.robo.api.commands.Scale;
import com.oberasoftware.robo.api.commands.SpeedCommand;
import com.oberasoftware.robo.api.servo.ServoCommand;
import com.oberasoftware.robo.dynamixel.DynamixelInstruction;
import com.oberasoftware.robo.dynamixel.DynamixelServoMovementHandler;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2Address;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2ReturnPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static com.oberasoftware.robo.core.ConverterUtil.intTo32BitByte;
import static com.oberasoftware.robo.core.ConverterUtil.toSafeInt;
import static com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket.bb2hex;

/**
 * @author Renze de Vries
 */
@Component
@ConditionalOnProperty(value = "protocol.v2.enabled", havingValue = "true", matchIfMissing = false)
public class DynamixelV2ServoMovementHandler extends AbstractV2MovementHandler implements EventHandler, DynamixelServoMovementHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelV2ServoMovementHandler.class);

    @Override
    @EventSubscribe
    public void receive(PositionCommand positionCommand) {
        LOG.debug("Received servo position command: {}", positionCommand);

        setGoal(positionCommand, positionCommand.getPosition(), positionCommand.getScale());
    }

    @Override
    @EventSubscribe
    public void receive(SpeedCommand speedCommand) {
        LOG.debug("Received a speed command: {}", speedCommand);

        setSpeed(speedCommand, speedCommand.getSpeed(), speedCommand.getScale());
    }

    @Override
    @EventSubscribe
    public void receive(PositionAndSpeedCommand command) {
        LOG.debug("Received a position and speed command: {}", command);

        setSpeed(command, command.getSpeed(), command.getSpeedScale());
        setGoal(command, command.getPosition(), command.getPositionScale());
    }


    private void setSpeed(ServoCommand command, int speed, Scale scale) {
        int servoId = toSafeInt(command.getServoId());
        DynamixelV2CommandPacket packet = new DynamixelV2CommandPacket(DynamixelInstruction.WRITE_DATA, servoId);
        if(scale.isValid(speed) || scale.isAbsolute()) {
            int convertedSpeed = scale.isAbsolute() ? speed : scale.convertToScale(speed, TARGET_SCALE_SPEED);
            LOG.info("Setting Servo: {} speed: {}", servoId, convertedSpeed);
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.put(intTo32BitByte(convertedSpeed));

            packet.addParam(DynamixelV2Address.GOAL_VELOCITY, buffer.array());

            byte[] dataToSend = packet.build();
            LOG.debug("Sending speed command: {}", bb2hex(dataToSend));

            LOG.debug("Sending package: {}", packet);
            byte[] received = connector.sendAndReceive(dataToSend);
            DynamixelV2ReturnPacket returnPacket = new DynamixelV2ReturnPacket(received);
            if(returnPacket.hasErrors()) {
                LOG.error("Received return package: {} errors detected: {} reason: {}",
                        returnPacket, returnPacket.hasErrors(), returnPacket.getErrorReason());
            }

            sleepUninterruptibly(100, TimeUnit.MILLISECONDS);
        }

    }
}
