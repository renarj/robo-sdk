package com.oberasoftware.robo.dynamixel.protocolv2.handlers;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.core.commands.VelocityModeCommand;
import com.oberasoftware.robo.dynamixel.DynamixelConnector;
import com.oberasoftware.robo.dynamixel.DynamixelInstruction;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2Address;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2ReturnPacket;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

import static com.oberasoftware.robo.core.ConverterUtil.intTo32BitByte;
import static com.oberasoftware.robo.core.ConverterUtil.toSafeInt;
import static com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket.bb2hex;
import static org.slf4j.LoggerFactory.getLogger;

@Component
@ConditionalOnProperty(value = "protocol.v2.enabled", havingValue = "true", matchIfMissing = false)
public class DynamixelV2AccelerationProfile implements EventHandler {
    private static final Logger LOG = getLogger(DynamixelV2AccelerationProfile.class);

    @Autowired
    private DynamixelConnector connector;

    @EventSubscribe
    public void receive(VelocityModeCommand command) {
        int servoId = toSafeInt(command.getServoId());
        LOG.debug("Received a servo mode: {}", command.getServoId(), command);

        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(intTo32BitByte(command.getAcceleration()));
        buffer.put(intTo32BitByte(command.getVelocity()));

        byte[] data = new DynamixelV2CommandPacket(DynamixelInstruction.WRITE_DATA, servoId)
                .addParam(DynamixelV2Address.PROFILE_ACCELERATION, buffer.array()).build();
        LOG.info("Sending acceleration and velocity data: {} to Servo: {}", bb2hex(data), servoId);

        byte[] received = connector.sendAndReceive(data);
        DynamixelV2ReturnPacket packet = new DynamixelV2ReturnPacket(received);
        if(packet.hasErrors()) {
            LOG.error("Could not send velocity and acceleration data: {} for servo: {}", bb2hex(received), servoId);
        }
    }
}