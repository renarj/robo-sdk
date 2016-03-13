package com.oberasoftware.robo.dynamixel.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.ServoCommand;
import com.oberasoftware.robo.dynamixel.*;
import com.oberasoftware.robo.api.commands.PositionAndSpeedCommand;
import com.oberasoftware.robo.api.commands.PositionCommand;
import com.oberasoftware.robo.api.commands.SpeedCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.oberasoftware.robo.core.ConverterUtil.intTo16BitByte;
import static com.oberasoftware.robo.core.ConverterUtil.toSafeInt;

/**
 * @author Renze de Vries
 */
@Component
public class DynamixelServoMovementHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelServoMovementHandler.class);

    private static final int NOT_SPECIFIED = -1;

    @Autowired
    private DynamixelConnector connector;

    @EventSubscribe
    public void receive(PositionCommand positionCommand) {
        LOG.debug("Received servo position command: {}", positionCommand);
        int servoId = toSafeInt(positionCommand.getServoId());

//        byte[] buffer = new DynamixelCommandPacket(DynamixelInstruction.WRITE_DATA, servoId)
//                .addParam(DynamixelAddress.GOAL_POSITION_L, intTo16BitByte(positionCommand.getPosition()))
//                .build();
//        byte[] received = connector.sendAndReceive(buffer);
//        LOG.debug("Received response: {}", new DynamixelReturnPacket(received));

        send(positionCommand, positionCommand.getPosition(), NOT_SPECIFIED);
    }

    @EventSubscribe
    public void receive(SpeedCommand speedCommand) {
        LOG.debug("Received a speed command: {}", speedCommand);
        int servoId = toSafeInt(speedCommand.getServoId());

//        connector.sendAndReceive(new DynamixelCommandPacket(DynamixelInstruction.WRITE_DATA, servoId)
//                .addParam(DynamixelAddress.MOVING_SPEED_L, intTo16BitByte(speedCommand.getSpeed()))
//                .build());
        send(speedCommand, NOT_SPECIFIED, speedCommand.getSpeed());
    }

    @EventSubscribe
    public void receive(PositionAndSpeedCommand command) {
        LOG.debug("Received a position and speed command: {}", command);
        int servoId = toSafeInt(command.getServoId());

//        connector.sendAndReceive(new DynamixelCommandPacket(DynamixelInstruction.WRITE_DATA, servoId)
//                .addParam(DynamixelAddress.MOVING_SPEED_L, intTo16BitByte(speedCommand.getSpeed()))
//                .build());
        send(command, command.getPosition(), command.getSpeed());
    }


    private void send(ServoCommand command, int goal, int speed) {
        int servoId = toSafeInt(command.getServoId());
        DynamixelCommandPacket packet = new DynamixelCommandPacket(DynamixelInstruction.WRITE_DATA, servoId);
        if(goal > NOT_SPECIFIED) {
            byte[] data;

            if(speed > NOT_SPECIFIED) {
                LOG.debug("Setting Servo: {} goal to: {}", servoId, goal);
                data = intTo16BitByte(goal);
            } else {
                LOG.debug("Setting Servo: {} goal to: {} and speed: {}", servoId, goal, speed);
                data = intTo16BitByte(goal, speed);
            }

            packet.addParam(DynamixelAddress.GOAL_POSITION_L, data);
        } else {
            LOG.debug("Setting Servo: {} speed to: {}", servoId, speed);
            packet.addParam(DynamixelAddress.MOVING_SPEED_L, intTo16BitByte(speed));
        }

        LOG.debug("Sending package: {}", packet);
        byte[] received = connector.sendAndReceive(packet.build());
        DynamixelReturnPacket returnPacket = new DynamixelReturnPacket(received);
        LOG.debug("Received return package: {} errors detected: {}", returnPacket, returnPacket.hasErrors());
    }
}
