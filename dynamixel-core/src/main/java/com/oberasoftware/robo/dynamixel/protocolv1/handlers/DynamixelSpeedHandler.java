package com.oberasoftware.robo.dynamixel.protocolv1.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.dynamixel.DynamixelAddress;
import com.oberasoftware.robo.dynamixel.DynamixelConnector;
import com.oberasoftware.robo.dynamixel.DynamixelInstruction;
import com.oberasoftware.robo.api.commands.SpeedCommand;
import com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1CommandPacket;
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
public class DynamixelSpeedHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelSpeedHandler.class);

    @Autowired
    private DynamixelConnector dynamixelConnector;

    @EventSubscribe
    public void receive(SpeedCommand speedCommand) {
        LOG.debug("Received a speed command: {}", speedCommand);
        int servoId = toSafeInt(speedCommand.getServoId());

        dynamixelConnector.sendAndReceive(new DynamixelV1CommandPacket(DynamixelInstruction.WRITE_DATA, servoId)
            .addParam(DynamixelAddress.MOVING_SPEED_L, intTo16BitByte(speedCommand.getSpeed()))
                .build());
    }
}
