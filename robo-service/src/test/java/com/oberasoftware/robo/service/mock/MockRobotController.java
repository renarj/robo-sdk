package com.oberasoftware.robo.service.mock;

import com.oberasoftware.robo.api.RobotController;
import com.oberasoftware.robo.api.Servo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Renze de Vries
 */
@Component
public class MockRobotController implements RobotController {
    private static final Logger LOG = LoggerFactory.getLogger(MockRobotController.class);

    private Map<String, Servo> servoMap = new HashMap<>();

    public MockRobotController() {
        servoMap.put("1", new MockServo("1", 0, 512, true));
        servoMap.put("2", new MockServo("2", 10, 128, true));
        servoMap.put("3", new MockServo("3", 10, 100, true));
        servoMap.put("4", new MockServo("4", 0, 900, false));
        servoMap.put("5", new MockServo("5", 0, 500, true));
    }

    @Override
    public boolean initialize() {
        return true;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public List<Servo> getServos() {
        return new ArrayList<>(servoMap.values());
    }

    @Override
    public Servo getServo(String servoId) {
        return servoMap.get(servoId);
    }
//
//    @Override
//    public void executeMotion(Motion motion) {
//        executeMotion(motion, 0);
//    }
//
//    @Override
//    public void executeMotion(Motion motion, int repeats) {
//        LOG.info("Executing motion: {} repeats: {}", motion, repeats);
//    }
}
