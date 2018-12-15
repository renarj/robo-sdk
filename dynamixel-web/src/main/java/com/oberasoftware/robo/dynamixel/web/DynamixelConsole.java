package com.oberasoftware.robo.dynamixel.web;

import com.oberasoftware.robo.dynamixel.DynamixelConfiguration;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@SpringBootApplication
@Import(DynamixelConfiguration.class)
public class DynamixelConsole {
    private static final Logger LOG = getLogger(DynamixelConsole.class);

    public static void main(String[] args) {
        LOG.info("Starting Dynamixel Web Console");
        ConfigurableApplicationContext c = SpringApplication.run(DynamixelConsole.class, args);

//        DynamixelServoDriver servoDriver = c.getBean(DynamixelServoDriver.class);
//        SerialDynamixelConnector connector = c.getBean(SerialDynamixelConnector.class);
    }
}
