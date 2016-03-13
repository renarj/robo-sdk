package com.oberasoftware.robo.dynamixel;

import com.google.common.util.concurrent.Uninterruptibles;
import com.oberasoftware.base.event.EventBus;
import com.oberasoftware.robo.api.RobotController;
import com.oberasoftware.robo.api.Servo;
import com.oberasoftware.robo.api.motion.MotionExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

/**
 * @author Renze de Vries
 */
@Component
public class DynamixelController implements RobotController {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelController.class);

    private static final int MAX_ID = 19;

    @Autowired
    private SerialDynamixelConnector connector;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Autowired
    private EventBus eventBus;

    @Autowired
    private MotionExecutor motionExecutor;

    private Map<Integer, DynamixelServo> servos = new HashMap<>();

    @Override
    public boolean initialize() {
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
//                        eventBus.registerHandler(servo);
                        servos.put(m, servo);
                    }
                } catch(IllegalArgumentException e) {
                    LOG.error("Could not read servo response on ping");
                }
            } else {
                LOG.debug("No Servo detected on ID: {}", m);
            }
        });

//        byte[] pos = connector.sendAndReceive(new DynamixelCommandPacket(DynamixelInstruction.READ_DATA, 10).addParam(DynamixelAddress.GOAL_POSITION_L, 2).build());
//        LOG.debug("Motor 10 position: {}", new DynamixelReturnPacket(pos));

        LOG.debug("Servos found: {}", servos.keySet());

        return !servos.isEmpty();
    }

    @Override
    public void shutdown() {

    }

    @Override
    public List<Servo> getServos() {
        return new ArrayList<>(servos.values());
    }

    @Override
    public Servo getServo(String servoId) {
        return servos.get(parseInt(servoId));
    }
//
//    @Override
//    public void executeMotion(Motion motion) {
//        executeMotion(motion, 0);
//    }
//
//    @Override
//    public void executeMotion(Motion motion, int repeats) {
//        LOG.debug("Received a motion request: {}", motion);
//        motionExecutor.execute(motion, repeats);
//    }
}
