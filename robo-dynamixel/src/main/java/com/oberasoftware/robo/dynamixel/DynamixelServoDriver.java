package com.oberasoftware.robo.dynamixel;

import com.google.common.util.concurrent.Uninterruptibles;
import com.oberasoftware.robo.api.Servo;
import com.oberasoftware.robo.api.ServoDriver;
import com.oberasoftware.robo.api.commands.BulkPositionSpeedCommand;
import com.oberasoftware.robo.api.commands.PositionAndSpeedCommand;
import com.oberasoftware.robo.api.exceptions.RoboException;
import com.oberasoftware.robo.dynamixel.handlers.DynamixelSyncWriteMovementHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author Renze de Vries
 */
@Component
public class DynamixelServoDriver implements ServoDriver {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelServoDriver.class);

    private static final int MAX_ID = 250;
    public static final String PORT = "port";

    @Autowired
    private SerialDynamixelConnector connector;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DynamixelSyncWriteMovementHandler syncWriteMovementHandler;

    private Map<String, Servo> servos = new HashMap<>();

    private String portName;

    @Override
    public void activate(Map<String, String> properties) {
        this.portName = properties.get(PORT);
        connector.connect(portName);

        if(!initialize()) {
            throw new RoboException("Could not load servos");
        }
    }

    @Override
    public void shutdown() {
        this.connector.disconnect();
    }

    private boolean initialize() {
        LOG.info("Starting servo scan");
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);

        IntStream motorRange = IntStream.range(1, MAX_ID);
        motorRange.forEach((m) -> {
            byte[] received = connector.sendAndReceive(new DynamixelCommandPacket(DynamixelInstruction.PING, m).build());
            if(received != null && received.length > 0) {
                try {
                    DynamixelReturnPacket packet = new DynamixelReturnPacket(received);
                    if (packet.getErrorCode() == 0) {
                        LOG.debug("Ping received from Servo: {}", m);

                        DynamixelServo servo = applicationContext.getBean(DynamixelServo.class, m);
                        servos.put(Integer.toString(m), servo);
                    }
                } catch(IllegalArgumentException e) {
                    LOG.error("Could not read servo response on ping");
                }
            } else {
                LOG.debug("No Servo detected on ID: {}", m);
            }
        });

        LOG.info("Servos found: {}", servos.keySet());
        return !servos.isEmpty();
    }

    @Override
    public boolean setServoSpeed(String servoId, int speed) {
        return false;
    }

    @Override
    public boolean setTargetPosition(String servoId, int targetPosition) {
        return false;
    }

    @Override
    public boolean setPositionAndSpeed(String servoId, int speed, int targetPosition) {
        return false;
    }

    @Override
    public boolean bulkSetPositionAndSpeed(Map<String, PositionAndSpeedCommand> commands) {
        syncWriteMovementHandler.receive(new BulkPositionSpeedCommand(commands));
        return true;
    }

    @Override
    public List<Servo> getServos() {
        return new ArrayList<>(servos.values());
    }
}
