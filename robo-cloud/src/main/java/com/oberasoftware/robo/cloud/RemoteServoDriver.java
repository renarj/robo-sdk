package com.oberasoftware.robo.cloud;

import com.oberasoftware.home.api.commands.BasicCommand;
import com.oberasoftware.home.api.model.BasicCommandBuilder;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.commands.PositionAndSpeedCommand;
import com.oberasoftware.robo.api.servo.Servo;
import com.oberasoftware.robo.api.servo.ServoDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Renze de Vries
 */
@Component
public class RemoteServoDriver implements ServoDriver {
    private static final Logger LOG = LoggerFactory.getLogger(RemoteServoDriver.class);

    private Robot robot;

    @Override
    public void activate(Robot robot, Map<String, String> properties) {
        LOG.info("Activating remote motion engine for robot: {}", robot.getName());
        this.robot = robot;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public boolean setServoSpeed(String servoId, int speed) {
        return false;
    }

    @Override
    public boolean setTargetPosition(String servoId, int targetPosition) {
        BasicCommand command = BasicCommandBuilder.create(robot.getName())
                .item("servos").label("position")
                .property("servoId", servoId)
                .property("position", Integer.toString(targetPosition))
                .build();

        robot.getRemoteDriver().publish(command);

        return true;
    }

    @Override
    public boolean setPositionAndSpeed(String servoId, int speed, int targetPosition) {
        return false;
    }

    @Override
    public boolean bulkSetPositionAndSpeed(Map<String, PositionAndSpeedCommand> commands) {
        return false;
    }

    @Override
    public boolean setTorgue(String servoId, int limit) {
        return false;
    }

    @Override
    public boolean setTorgue(String servoId, boolean state) {
        return false;
    }

    @Override
    public List<Servo> getServos() {
        return null;
    }

    @Override
    public Servo getServo(String servoId) {
        return null;
    }
}
