package com.oberasoftware.robo.dynamixel.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.dynamixel.DynamixelAddress;
import com.oberasoftware.robo.dynamixel.DynamixelCommandPacket;
import com.oberasoftware.robo.dynamixel.DynamixelConnector;
import com.oberasoftware.robo.dynamixel.DynamixelInstruction;
import com.oberasoftware.robo.api.commands.TorgueCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.oberasoftware.robo.core.ConverterUtil.toSafeInt;

/**
 * @author Renze de Vries
 */
@Component
public class DynamixelTorgueHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelTorgueHandler.class);

    @Autowired
    private DynamixelConnector connector;

    @EventSubscribe
    public void receive(TorgueCommand torgueCommand) {
        LOG.debug("Received a torgue command: {}", torgueCommand);
        int servoId = toSafeInt(torgueCommand.getServoId());

        int targetTorgueState = 0x00;
        if(torgueCommand.isEnableTorque()) {
            targetTorgueState = 0x01;
        }

        LOG.debug("Setting torgue to: {} for servo: {}", targetTorgueState, servoId);

        connector.sendAndReceive(new DynamixelCommandPacket(DynamixelInstruction.WRITE_DATA, servoId)
                .addParam(DynamixelAddress.TORGUE_ENABLE, targetTorgueState)
                .build());

    }
}
