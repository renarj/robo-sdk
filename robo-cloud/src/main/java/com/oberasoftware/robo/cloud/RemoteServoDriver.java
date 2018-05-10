package com.oberasoftware.robo.cloud;

import com.oberasoftware.robo.api.commands.BasicCommand;
import com.oberasoftware.robo.api.commands.Scale;
import com.oberasoftware.robo.core.model.BasicCommandBuilder;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.commands.PositionAndSpeedCommand;
import com.oberasoftware.robo.api.servo.Servo;
import com.oberasoftware.robo.api.servo.ServoCommand;
import com.oberasoftware.robo.api.servo.ServoDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Renze de Vries
 */
@Component
@Scope("prototype")
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
    public boolean setServoSpeed(String servoId, int speed, Scale scale) {
        return false;
    }

    @Override
    public boolean setTargetPosition(String servoId, int targetPosition, Scale scale) {
        BasicCommand command = BasicCommandBuilder.create(robot.getName())
                .item("servos").label("position")
                .property("servoId", servoId)
                .property("position", Integer.toString(targetPosition))
                .build();

        robot.getRemoteDriver().publish(command);

        return true;
    }

    @Override
    public boolean supportsCommand(ServoCommand servoCommand) {
        return false;
    }

    @Override
    public boolean sendCommand(ServoCommand servoCommand) {
        return false;
    }

    @Override
    public boolean setPositionAndSpeed(String servoId, int speed, Scale speedScale, int targetPosition, Scale positionScale) {
        BasicCommand command = BasicCommandBuilder.create(robot.getName())
                .item("servos").label("position")
                .property("servoId", servoId)
                .property("position", Integer.toString(targetPosition))
                .property("speed", Integer.toString(speed))
                .build();

        robot.getRemoteDriver().publish(command);

        return true;
    }

    @Override
    public boolean bulkSetPositionAndSpeed(Map<String, PositionAndSpeedCommand> commands) {
        return false;
    }

    @Override
    public boolean setTorgue(String servoId, int limit) {
        BasicCommand command = BasicCommandBuilder.create(robot.getName())
                .item("servos").label("torgue")
                .property("servoId", servoId)
                .property("torgue", Boolean.toString(true))
                .property("torgueLimit", Integer.toString(limit))
                .build();

        robot.getRemoteDriver().publish(command);

        return true;
    }

    @Override
    public boolean setTorgue(String servoId, boolean state) {
        BasicCommand command = BasicCommandBuilder.create(robot.getName())
                .item("servos").label("torgue")
                .property("servoId", servoId)
                .property("torgue", Boolean.toString(state))
                .build();

        robot.getRemoteDriver().publish(command);

        return true;
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
