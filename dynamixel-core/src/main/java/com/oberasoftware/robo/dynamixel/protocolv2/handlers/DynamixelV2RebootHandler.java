package com.oberasoftware.robo.dynamixel.protocolv2.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.core.commands.RebootCommand;
import com.oberasoftware.robo.dynamixel.DynamixelConnector;
import com.oberasoftware.robo.dynamixel.DynamixelInstruction;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2ReturnPacket;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static com.oberasoftware.robo.core.ConverterUtil.toSafeInt;
import static com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1CommandPacket.bb2hex;
import static org.slf4j.LoggerFactory.getLogger;

@Component
@ConditionalOnProperty(value = "protocol.v2.enabled", havingValue = "true", matchIfMissing = false)
public class DynamixelV2RebootHandler implements EventHandler {
    private static final Logger LOG = getLogger(DynamixelV2RebootHandler.class);

    @Autowired
    private DynamixelConnector connector;

    @EventSubscribe
    public void receive(RebootCommand command) {
        int servoId = toSafeInt(command.getServoId());
        LOG.debug("Received a servo: {} reboot command", command.getServoId());

        byte[] data = new DynamixelV2CommandPacket(DynamixelInstruction.REBOOT, servoId).build();
        LOG.debug("Sending Servo: {} reboot command: {}", servoId, bb2hex(data));

        byte[] received = connector.sendAndReceive(data);
        DynamixelV2ReturnPacket rp = new DynamixelV2ReturnPacket(received);
        if(rp.hasErrors()) {
            LOG.error("Reboot for servo: {} has failed: {}", servoId, bb2hex(received));
        }

    }
}
