package com.oberasoftware.robo.dynamixel.web;

import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.RobotRegistry;
import com.oberasoftware.robo.api.servo.ServoDriver;
import com.oberasoftware.robo.dynamixel.commands.DynamixelAngleLimitCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Renze de Vries
 */
@RestController
@RequestMapping("/servos")
public class ServoController {
    private static final Logger LOG = LoggerFactory.getLogger(ServoController.class);

    @Autowired
    private RobotRegistry robotRegistry;

    @Autowired
    private ServoDriver servoDriver;

    @RequestMapping
    public List<SimpleServo> getServos() {
        List<Robot> robots = robotRegistry.getRobots();
        return robots.stream().map(r -> r.getServoDriver().getServos()
                .stream().map(SimpleServo::new).collect(Collectors.toList()))
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    @RequestMapping(value = "/set/{servoId}/position/{position}", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    public SimpleServo setServoPosition(@PathVariable  String servoId, @PathVariable int position) {
        LOG.info("Setting servo: {} to position: {}", servoId, position);
        getServoDriver().setTargetPosition(servoId, position);

        return new SimpleServo(getServoDriver().getServo(servoId));
    }

    @RequestMapping(value = "/set/{servoId}/speed/{speed}", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    public SimpleServo setServoSpeed(@PathVariable  String servoId, @PathVariable int speed) {
        LOG.info("Setting servo: {} speed: {}", servoId, speed);
        getServoDriver().setServoSpeed(servoId, speed);

        return new SimpleServo(getServoDriver().getServo(servoId));
    }

    @RequestMapping(value = "/set/{servoId}/torgue/{torgue}", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    public SimpleServo setServoTorgue(@PathVariable  String servoId, @PathVariable int torgue) {
        LOG.info("Setting servo: {} Torgue: {}", servoId, torgue);
        getServoDriver().setTorgue(servoId, torgue);

        return new SimpleServo(getServoDriver().getServo(servoId));
    }

    @RequestMapping(value = "/set/{servoId}/angle/{min}/{max}", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    public SimpleServo setAngleLimits(@PathVariable  String servoId, @PathVariable int min, @PathVariable int max) {
        LOG.info("Setting servo: {} angle Limits min: {} max: {}", servoId, min, max);
        getServoDriver().sendCommand(new DynamixelAngleLimitCommand(servoId, min, max));

        return new SimpleServo(getServoDriver().getServo(servoId));
    }


    @RequestMapping(value = "/enable/{servoId}/torgue", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    public SimpleServo enableTorgue(@PathVariable  String servoId) {
        getServoDriver().setTorgue(servoId, true);

        return new SimpleServo(getServoDriver().getServo(servoId));
    }

    @RequestMapping(value = "/disable/{servoId}/torgue", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    public SimpleServo disableTorgue(@PathVariable  String servoId) {
        getServoDriver().setTorgue(servoId, false);

        return new SimpleServo(getServoDriver().getServo(servoId));
    }

    @RequestMapping(value = "/enable/torgue", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    public void enableTorgue() {
        LOG.info("Enabling torgue on all servos");
        getServoDriver().getServos().forEach(s -> getServoDriver().setTorgue(s.getId(), true));
    }

    @RequestMapping(value = "/disable/torgue", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    public void disableTorgue() {
        LOG.info("Disabling torgue on all servos");
        getServoDriver().getServos().forEach(s -> getServoDriver().setTorgue(s.getId(), false));
    }


    private Robot getDefaultRobot() {
        return robotRegistry.getRobots().get(0);
    }

    private ServoDriver getServoDriver() {
        return getDefaultRobot().getServoDriver();
    }
}
