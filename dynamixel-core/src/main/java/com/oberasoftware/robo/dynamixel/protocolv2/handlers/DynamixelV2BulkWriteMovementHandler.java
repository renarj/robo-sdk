package com.oberasoftware.robo.dynamixel.protocolv2.handlers;


import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.commands.BulkPositionSpeedCommand;
import com.oberasoftware.robo.api.commands.PositionAndSpeedCommand;
import com.oberasoftware.robo.api.commands.Scale;
import com.oberasoftware.robo.dynamixel.BulkWriteMovementHandler;
import com.oberasoftware.robo.dynamixel.DynamixelInstruction;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2Address;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.oberasoftware.robo.core.ConverterUtil.toSafeInt;
import static com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket.bb2hex;
import static org.slf4j.LoggerFactory.getLogger;

@Component
@ConditionalOnProperty(value = "protocol.v2.enabled", havingValue = "true", matchIfMissing = false)
public class DynamixelV2BulkWriteMovementHandler extends AbstractV2MovementHandler implements BulkWriteMovementHandler {
    private static final Logger LOG = getLogger(DynamixelV2BulkWriteMovementHandler.class);

    @Override
    @EventSubscribe
    public void receive(BulkPositionSpeedCommand command) {
        Map<String, PositionAndSpeedCommand> commands = command.getCommands();
        LOG.info("Received a bulk position command: {} with mode: {}", commands, command.getMode());

        switch(command.getMode()) {
            case SYNC:
                moveSyncWrite(commands);
                break;
            case REGISTERED_WRITE:
                moveRegWrite(commands);
                break;
            default:
                moveDirect(commands);
        }
    }

    private void moveSyncWrite(Map<String, PositionAndSpeedCommand> commands) {
        DynamixelV2CommandPacket pkt = new DynamixelV2CommandPacket(DynamixelInstruction.SYNC_WRITE, 0xFE);
        pkt.addParam(DynamixelV2Address.PROFILE_ACCELERATION, (byte)0x0C, (byte)0x0);

        commands.values().forEach(c -> {
            int servoId = toSafeInt(c.getServoId());
            int goal = c.getPosition();
            int velocity = c.getSpeed();

            Scale scale = c.getPositionScale();
            if(scale.isValid(goal) || scale.isAbsolute()) {
                int convertedGoal = scale.isAbsolute() ? goal : scale.convertToScale(goal, TARGET_SCALE_POSITION);

                pkt.add8BitParam(servoId);
                pkt.add32BitParam(0);
                pkt.add32BitParam(velocity);
                pkt.add32BitParam(convertedGoal);
            } else {
                LOG.error("Ignoring position for servo: {} because invalid target position: {} for scale: {}", servoId, goal, scale);
            }
        });

        byte[] dataToSend = pkt.build();
        LOG.info("Sending Sync write movement command: {}", bb2hex(dataToSend));

        //sync write is send and forget
        connector.sendNoReceive(dataToSend);
    }

    private void moveRegWrite(Map<String, PositionAndSpeedCommand> commands) {
        commands.forEach((k, v) -> {
            LOG.debug("Writing registered action for servo: {} for position: {}", k, v.getPosition());
            setGoalWithRegWrite(v, v.getPosition(), v.getPositionScale());
        });
        broadcastAction();
    }

    private void moveDirect(Map<String, PositionAndSpeedCommand> commands) {
        commands.forEach((k, v) -> {
            LOG.info("Setting direct position: {} for Servo: {}", v, k);
            super.setGoal(v, v.getPosition(), v.getPositionScale());
        });
    }
}
