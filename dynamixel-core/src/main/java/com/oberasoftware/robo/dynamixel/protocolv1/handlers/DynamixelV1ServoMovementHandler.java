package com.oberasoftware.robo.dynamixel.protocolv1.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.commands.PositionAndSpeedCommand;
import com.oberasoftware.robo.api.commands.PositionCommand;
import com.oberasoftware.robo.api.commands.SpeedCommand;
import com.oberasoftware.robo.api.servo.ServoCommand;
import com.oberasoftware.robo.dynamixel.*;
import com.oberasoftware.robo.dynamixel.DynamixelCommandPacket;
import com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1ReturnPacket;
import com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1CommandPacket;
import com.oberasoftware.robo.dynamixel.DynamixelServoMovementHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static com.oberasoftware.robo.core.ConverterUtil.intTo16BitByte;
import static com.oberasoftware.robo.core.ConverterUtil.toSafeInt;

/**
 * @author Renze de Vries
 */
@Component
@ConditionalOnProperty(value = "protocol.v2.enabled", havingValue = "false", matchIfMissing = true)
public class DynamixelV1ServoMovementHandler implements DynamixelServoMovementHandler, EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelV1ServoMovementHandler.class);

    private static final int NOT_SPECIFIED = -1;

    @Autowired
    private DynamixelConnector connector;

    @EventSubscribe
    public void receive(PositionCommand positionCommand) {
        LOG.debug("Received servo position command: {}", positionCommand);

        send(positionCommand, positionCommand.getPosition(), NOT_SPECIFIED);
    }

    @EventSubscribe
    public void receive(SpeedCommand speedCommand) {
        LOG.debug("Received a speed command: {}", speedCommand);

        send(speedCommand, NOT_SPECIFIED, speedCommand.getSpeed());
    }

    @EventSubscribe
    public void receive(PositionAndSpeedCommand command) {
        LOG.debug("Received a position and speed command: {}", command);

        send(command, command.getPosition(), command.getSpeed());
    }


    private void send(ServoCommand command, int goal, int speed) {
        int servoId = toSafeInt(command.getServoId());
        DynamixelCommandPacket packet = new DynamixelV1CommandPacket(DynamixelInstruction.WRITE_DATA, servoId);
        if(goal > NOT_SPECIFIED) {
            byte[] data;

            if(speed > NOT_SPECIFIED) {
                LOG.debug("Setting Servo: {} goal to: {} and speed: {}", servoId, goal, speed);
                data = intTo16BitByte(goal, speed);
            } else {
                LOG.debug("Setting Servo: {} goal to: {}", servoId, goal);
                data = intTo16BitByte(goal);
            }

            packet.addParam(DynamixelAddress.GOAL_POSITION_L, data);
        } else {
            LOG.debug("Setting Servo: {} speed to: {}", servoId, speed);
            packet.addParam(DynamixelAddress.MOVING_SPEED_L, intTo16BitByte(speed));
        }

        LOG.debug("Sending package: {}", packet);
        byte[] received = connector.sendAndReceive(packet.build());
        DynamixelV1ReturnPacket returnPacket = new DynamixelV1ReturnPacket(received);
        LOG.debug("Received return package: {} errors detected: {} reason: {}",
                returnPacket, returnPacket.hasErrors(), returnPacket.getErrorReason());
    }
}
