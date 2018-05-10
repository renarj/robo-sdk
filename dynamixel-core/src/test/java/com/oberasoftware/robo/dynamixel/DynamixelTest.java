package com.oberasoftware.robo.dynamixel;

import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.Uninterruptibles;
import com.oberasoftware.robo.api.servo.ServoDriver;
import com.oberasoftware.robo.api.servo.ServoProperty;
import com.oberasoftware.robo.core.commands.AngleLimitCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import java.util.concurrent.TimeUnit;

/**
 * @author Renze de Vries
 */
@SpringBootApplication
@Import(DynamixelConfiguration.class)
public class DynamixelTest {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelTest.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(DynamixelTest.class);
        ServoDriver servoDriver = context.getBean(ServoDriver.class);
        servoDriver.activate(null, ImmutableMap.<String, String>builder().put(DynamixelServoDriver.PORT, "/dev/tty.usbmodem1441").build());
        servoDriver.getServos().forEach(s -> {
            LOG.info("Servo found: {} on position: {}", s.getId(), s.getData().getValue(ServoProperty.POSITION));
        });
//        servoDriver.sendCommand(new AngleLimitCommand("6", AngleLimitCommand.MODE.WHEEL_MODE));
//        servoDriver.sendCommand(new AngleLimitCommand("14", AngleLimitCommand.MODE.WHEEL_MODE));
//        servoDriver.sendCommand(new AngleLimitCommand("16", AngleLimitCommand.MODE.WHEEL_MODE));
//        servoDriver.sendCommand(new AngleLimitCommand("6", AngleLimitCommand.MODE.WHEEL_MODE));

//        servoDriver.setServoSpeed("6", 300);
//        servoDriver.setServoSpeed("14", 300);
//        servoDriver.setServoSpeed("16", 300);
//        servoDriver.setServoSpeed("6", 300);
        Uninterruptibles.sleepUninterruptibly(30, TimeUnit.SECONDS);
//        servoDriver.setServoSpeed("6", 0);
//        servoDriver.setServoSpeed("14", 0);
//        servoDriver.setServoSpeed("16", 0);
//        servoDriver.setServoSpeed("6", 0);
        LOG.info("Why is it not freaking working yet?");

    }


}
