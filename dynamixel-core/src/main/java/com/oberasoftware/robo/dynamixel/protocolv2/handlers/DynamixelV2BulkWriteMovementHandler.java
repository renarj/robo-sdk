package com.oberasoftware.robo.dynamixel.protocolv2.handlers;


import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.commands.BulkPositionSpeedCommand;
import com.oberasoftware.robo.api.commands.PositionAndSpeedCommand;
import com.oberasoftware.robo.dynamixel.BulkWriteMovementHandler;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

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
                break;
            case REGISTERED_WRITE:
                moveRegWrite(commands);
                break;
            default:
                moveDirect(commands);
        }
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
