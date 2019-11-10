package com.oberasoftware.robo.dynamixel.protocolv1.handlers;

import com.google.common.collect.ImmutableMap;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.servo.ServoProperty;
import com.oberasoftware.robo.api.servo.events.ServoDataReceivedEvent;
import com.oberasoftware.robo.core.ServoDataImpl;
import com.oberasoftware.robo.core.commands.ReadTemperatureCommand;
import com.oberasoftware.robo.dynamixel.*;
import com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1ReturnPacket;
import com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1CommandPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.oberasoftware.robo.core.ConverterUtil.toSafeInt;
import static com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1CommandPacket.bb2hex;
import static java.lang.String.valueOf;

/**
 * @author Renze de Vries
 */
@Component
@ConditionalOnProperty(value = "protocol.v2.enabled", havingValue = "false", matchIfMissing = true)
public class DynamixelV1ReadTemperatureHandler implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelV1ReadTemperatureHandler.class);

    @Autowired
    private DynamixelConnector connector;

    @EventSubscribe
    public ServoDataReceivedEvent receive(ReadTemperatureCommand command) {
        LOG.debug("Received a read tempeterure command: {}", command);

        int servoId = toSafeInt(command.getServoId());

        byte[] data = new DynamixelV1CommandPacket(DynamixelInstruction.READ_DATA, servoId)
                .addParam(DynamixelAddress.CURRENT_VOLTAGE, 0x02)
                .build();
        byte[] received = connector.sendAndReceive(data);
        DynamixelV1ReturnPacket returnPacket = new DynamixelV1ReturnPacket(received);
        if(!returnPacket.hasErrors()) {
            LOG.trace("Received a voltage and temperature reply: {} for servo: {}", bb2hex(received), servoId);

            byte[] params = returnPacket.getParameters();
            if(params.length == 2) {
                double voltage = (params[0] / (double)10);
                int temperature = params[1];

                LOG.debug("Servo: {} has temperature: {} and voltage: {}", servoId, temperature, voltage);

                Map<ServoProperty, Object> map = new ImmutableMap.Builder<ServoProperty, Object>()
                        .put(ServoProperty.TEMPERATURE, temperature)
                        .put(ServoProperty.VOLTAGE, voltage)
                        .build();

                return new ServoDataReceivedEvent(valueOf(servoId), new ServoDataImpl(command.getServoId(), map));
            } else {
                LOG.warn("Incorrect number of parameters in return package was: {}", bb2hex(params));
            }
        } else {
            LOG.warn("Received an error: {} for speed and position for servo: {}", returnPacket.getErrorCode(), servoId);
        }


        return null;

    }
}
