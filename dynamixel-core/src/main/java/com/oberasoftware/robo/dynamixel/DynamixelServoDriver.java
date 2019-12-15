package com.oberasoftware.robo.dynamixel;

import com.google.common.base.Stopwatch;
import com.oberasoftware.base.event.impl.LocalEventBus;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.commands.*;
import com.oberasoftware.robo.api.servo.Servo;
import com.oberasoftware.robo.api.servo.ServoCommand;
import com.oberasoftware.robo.api.servo.ServoDriver;
import com.oberasoftware.robo.core.ConverterUtil;
import com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1CommandPacket;
import com.oberasoftware.robo.dynamixel.protocolv1.DynamixelV1ReturnPacket;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2CommandPacket;
import com.oberasoftware.robo.dynamixel.protocolv2.DynamixelV2ReturnPacket;
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

    private static final int MAX_ID = 240;
    public static final String PORT = "port";
    public static final int OPENCR_MODEL = 116;

    @Autowired
    private SerialDynamixelConnector connector;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private BulkWriteMovementHandler bulkWriteMovementHandler;

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

        boolean motorsFound;
        if(properties.containsKey("motors")) {
            String motorIdentifiers = properties.get("motors");

            motorsFound = !scan(parseMotorIdentifiers(motorIdentifiers), true).isEmpty();
        } else {
            motorsFound = !scan().isEmpty();
        }

        if(!motorsFound) {
            LOG.error("Could not find any servos");
        }
    }

    private IntStream parseMotorIdentifiers(String motorIdentifiers) {
        String[] motorIds = motorIdentifiers.split(",");
        int[] servoIds = new int[motorIds.length];
        for(int i=0; i<motorIds.length; i++) {
            String id = motorIds[i].trim();
            if(id.length() > 0) {
                servoIds[i] = ConverterUtil.toSafeInt(id);
            }
        }
        return IntStream.of(servoIds);
    }

    @Override
    public void shutdown() {
        this.connector.disconnect();
    }

    public List<Servo> scan() {
        return scan(IntStream.range(1, MAX_ID), false);
    }

    public List<Servo> scan(IntStream motorRange, boolean register) {
        LOG.info("Starting servo scan from stream: {}", motorRange);

        List<Servo> foundServos = new ArrayList<>();
        motorRange.forEach((m) -> {
            byte[] data = new DynamixelV1CommandPacket(DynamixelInstruction.PING, m).build();
            if(v2Enabled) {
                data = new DynamixelV2CommandPacket(DynamixelInstruction.PING, m).build();
            }
            Stopwatch w = Stopwatch.createStarted();
            byte[] received = connector.sendAndReceive(data);
            w.stop();

            if(received != null && received.length > 0) {
                try {
                    LOG.debug("Received: {} in {} ms.", bb2hex(received), w.elapsed(TimeUnit.MILLISECONDS));
                    DynamixelReturnPacket packet;
                    if(v2Enabled) {
                        packet = new DynamixelV2ReturnPacket(received);
                    } else {
                        packet = new DynamixelV1ReturnPacket(received);
                    }
                    if (packet.getErrorCode() == 0) {
                        LOG.info("Ping received from Servo: {} with data: {}", m, bb2hex(received));

                        byte[] params = packet.getParameters();


                        int modelNr = ConverterUtil.byteToInt(params[0], params[1]);
                        LOG.debug("We have model number: {} for ID: {}", modelNr, m);

                        if(modelNr != OPENCR_MODEL) {
                            DynamixelServo servo = applicationContext.getBean(DynamixelServo.class, m, modelNr);
                            foundServos.add(servo);

                            if(register) {
                                servos.put(Integer.toString(m), servo);
                            }
                        } else {
                            LOG.info("Found an Open CR board on ID: {}", m);
                        }
                    }
                } catch(IllegalArgumentException e) {
                    LOG.error("Could not read servo response on ping");
                }
            } else {
                LOG.debug("No Servo detected on ID: {} in {} ms.", m, w.elapsed(TimeUnit.MILLISECONDS));
            }
        });

        LOG.info("Servos found: {}", servos.keySet());
        return foundServos;

    }

//    private int getModelInformation(int motorId) {
//        byte[] modelInfoData = new DynamixelV2CommandPacket(DynamixelInstruction.READ_DATA, motorId)
//                .addInt16Bit(DynamixelV2Address.MODEL_INFORMATION, 0x02).build();
//        byte[] received = connector.sendAndReceive(modelInfoData);
//        DynamixelReturnPacket packet;
//
//        if(v2Enabled) {
//            packet = new DynamixelV2ReturnPacket(received);
//        } else {
//            packet = new DynamixelV1ReturnPacket(received);
//        }
//
//
//    }

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
    public boolean setServoSpeed(String servoId, int speed, Scale scale) {
        servoMovementHandler.receive(new SpeedCommand(servoId, speed, scale));

        return false;
    }

    @Override
    public boolean setTargetPosition(String servoId, int targetPosition, Scale scale) {
        servoMovementHandler.receive(new PositionCommand(servoId, targetPosition, scale));

        return true;
    }

    @Override
    public boolean setPositionAndSpeed(String servoId, int speed, Scale speedScale, int targetPosition, Scale positionScale) {
        servoMovementHandler.receive(new PositionAndSpeedCommand(servoId, targetPosition, positionScale, speed, speedScale));

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
    public boolean setTorgueAll(boolean state) {
        torgueHandler.receive(new BulkTorgueCommand(state));
        return true;
    }

    @Override
    public boolean bulkSetPositionAndSpeed(Map<String, PositionAndSpeedCommand> commands) {
        return bulkSetPositionAndSpeed(commands, BulkPositionSpeedCommand.WRITE_MODE.SYNC);
    }

    @Override
    public boolean bulkSetPositionAndSpeed(Map<String, PositionAndSpeedCommand> commands, BulkPositionSpeedCommand.WRITE_MODE mode) {
        bulkWriteMovementHandler.receive(new BulkPositionSpeedCommand(commands, mode));
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
