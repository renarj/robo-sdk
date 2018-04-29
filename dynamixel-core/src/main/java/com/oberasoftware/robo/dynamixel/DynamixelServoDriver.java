package com.oberasoftware.robo.dynamixel;

import com.google.common.util.concurrent.Uninterruptibles;
import com.oberasoftware.base.event.impl.LocalEventBus;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.commands.*;
import com.oberasoftware.robo.api.exceptions.RoboException;
import com.oberasoftware.robo.api.servo.Servo;
import com.oberasoftware.robo.api.servo.ServoCommand;
import com.oberasoftware.robo.api.servo.ServoDriver;
import com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1CommandPacket;
import com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1ReturnPacket;
import com.oberasoftware.robo.dynamixel.protocolv1.handlers.DynamixelSyncWriteMovementHandler;
import com.oberasoftware.robo.dynamixel.protocolv1.handlers.DynamixelV1TorgueHandler;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2ReturnPacket;
import com.oberasoftware.robo.dynamixel.protocolv2.handlers.DynamixelServoMovementHandler;
import com.oberasoftware.robo.dynamixel.protocolv2.handlers.DynamixelTorgueHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket.bb2hex;

/**
 * @author Renze de Vries
 */
@Component
@Primary
public class DynamixelServoDriver implements ServoDriver {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelServoDriver.class);

    private static final int MAX_ID = 249;
    public static final String PORT = "port";

    @Autowired
    private SerialDynamixelConnector connector;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DynamixelSyncWriteMovementHandler syncWriteMovementHandler;

    @Autowired
    private DynamixelServoMovementHandler servoMovementHandler;

    @Autowired
    private DynamixelTorgueHandler torgueHandler;

    @Value("${protocol.v2.enabled:false}")
    private boolean v2Enabled;

    @Autowired
    private LocalEventBus eventBus;

    private Map<String, Servo> servos = new HashMap<>();

    private String portName;

    @Override
    public void activate(Robot robot, Map<String, String> properties) {
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
            byte[] data = new DynamixelV1CommandPacket(DynamixelInstruction.PING, m).build();
            if(v2Enabled) {
                data = new DynamixelV2CommandPacket(DynamixelInstruction.PING, m).build();
            }
            byte[] received = connector.sendAndReceive(data);

            //new byte[] {(byte)0xff, (byte)0xff, (byte)0xfd, (byte)0x00, (byte)1, (byte)0x03, (byte)0x00, (byte)0x01, (byte)0x19, (byte)0x4e})
            if(received != null && received.length > 0) {
                try {
                    LOG.debug("Recveived: {}", bb2hex(received));
                    DynamixelReturnPacket packet;
                    if(v2Enabled) {
                        packet = new DynamixelV2ReturnPacket(received);
                    } else {
                        packet = new DynamixelV1ReturnPacket(received);
                    }
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
    public boolean supportsCommand(ServoCommand servoCommand) {

        return false;
    }

    @Override
    public boolean sendCommand(ServoCommand servoCommand) {
        eventBus.publishSync(servoCommand, TimeUnit.MINUTES, 2);
        return false;
    }

    @Override
    public boolean setServoSpeed(String servoId, int speed) {
        servoMovementHandler.receive(new SpeedCommand(servoId, speed));

        return false;
    }

    @Override
    public boolean setTargetPosition(String servoId, int targetPosition) {
        servoMovementHandler.receive(new PositionCommand(servoId, targetPosition));

        return true;
    }

    @Override
    public boolean setPositionAndSpeed(String servoId, int speed, int targetPosition) {
        servoMovementHandler.receive(new PositionAndSpeedCommand(servoId, targetPosition, speed));

        return true;
    }

    @Override
    public boolean setTorgue(String s, int limit) {
        torgueHandler.receive(new TorgueCommand(s, true));
        torgueHandler.receive(new TorgueLimitCommand(s, limit));
        return true;
    }

    @Override
    public boolean setTorgue(String s, boolean b) {
        torgueHandler.receive(new TorgueCommand(s, b));
        return true;
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

    @Override
    public Servo getServo(String s) {
        return servos.get(s);
    }
}
