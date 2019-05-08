package com.oberasoftware.robo.dynamixel.protocolv2.handlers;

import com.oberasoftware.robo.api.commands.Scale;
import com.oberasoftware.robo.api.servo.ServoCommand;
import com.oberasoftware.robo.dynamixel.DynamixelConnector;
import com.oberasoftware.robo.dynamixel.DynamixelInstruction;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2Address;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2ReturnPacket;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

import static com.oberasoftware.robo.core.ConverterUtil.intTo32BitByte;
import static com.oberasoftware.robo.core.ConverterUtil.toSafeInt;
import static com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket.bb2hex;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public abstract class AbstractV2MovementHandler {
    private static final Logger LOG = getLogger(AbstractV2MovementHandler.class);

    protected static final Scale TARGET_SCALE_SPEED = new Scale(-400, 400);
    protected static final Scale TARGET_SCALE_POSITION = new Scale(0, 4095);
    private static final int BROADCAST_ID = 0xFE;

    @Autowired
    protected DynamixelConnector connector;

    protected void setGoal(ServoCommand command, int goal, Scale scale) {
        setGoalWithNormalWrite(command, goal, scale);
    }

    protected void setGoalWithNormalWrite(ServoCommand command, int goal, Scale scale) {
        setWithWriteInstruction(command, goal, scale, DynamixelInstruction.WRITE_DATA);
    }

    protected void setGoalWithRegWrite(ServoCommand command, int goal, Scale scale) {
        setWithWriteInstruction(command, goal, scale, DynamixelInstruction.REG_WRITE);
    }

    protected void broadcastAction() {
        LOG.info("Sending broadcast action packet");

        byte[] data = new DynamixelV2CommandPacket(DynamixelInstruction.ACTION, BROADCAST_ID).build();
        LOG.debug("Sending broadcast package: {}", bb2hex(data));
        connector.sendAndReceive(data);
    }

    private void setWithWriteInstruction(ServoCommand command, int goal, Scale scale, DynamixelInstruction instruction) {
        int servoId = toSafeInt(command.getServoId());
        DynamixelV2CommandPacket packet = new DynamixelV2CommandPacket(instruction, servoId);

        if(scale.isValid(goal) || scale.isAbsolute()) {
            int convertedGoal = scale.isAbsolute() ? goal : scale.convertToScale(goal, TARGET_SCALE_POSITION);
            LOG.info("Setting Servo: {} goal to: {}", servoId, convertedGoal);
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.put(intTo32BitByte(convertedGoal));

            packet.addParam(DynamixelV2Address.GOAL_POSITION_L, buffer.array());

            byte[] dataToSend = packet.build();
            LOG.debug("Sending movement command: {}", bb2hex(dataToSend));

            byte[] received = connector.sendAndReceive(dataToSend);
            DynamixelV2ReturnPacket returnPacket = new DynamixelV2ReturnPacket(received);
            LOG.debug("Received return package: {} errors detected: {} reason: {}",
                    returnPacket, returnPacket.hasErrors(), returnPacket.getErrorReason());
        } else {
            LOG.warn("Goal: {} specified for servo: {} is invalid for scale: {}", goal, servoId, scale);
        }
    }
}
