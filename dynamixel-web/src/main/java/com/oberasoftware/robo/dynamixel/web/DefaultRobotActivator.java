package com.oberasoftware.robo.dynamixel.web;

import com.google.common.collect.ImmutableMap;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.core.SpringAwareRobotBuilder;
import com.oberasoftware.robo.core.sensors.ServoSensorDriver;
import com.oberasoftware.robo.dynamixel.DynamixelServoDriver;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
//@ConditionalOnProperty(value = "dynamixel.port", havingValue = "true", matchIfMissing = false)
public class DefaultRobotActivator {
    private static final Logger LOG = getLogger(DefaultRobotActivator.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${dynamixel.port:}")
    private String optionalPort;

    @PostConstruct
    public void postConstruct() {
        if (!StringUtils.isEmpty(optionalPort)) {
            activate(optionalPort);
        } else {
            LOG.info("No port was specified for driver, skipping auto configuration");
        }
    }

    private void activate(String port) {
        LOG.info("Activating port: {}", optionalPort);
        Map<String, String> servoProperties = ImmutableMap.<String, String>builder()
                .put("port", port).build();

        Robot robot = new SpringAwareRobotBuilder("default", applicationContext)
                .servoDriver(DynamixelServoDriver.class, servoProperties)
                .capability(ServoSensorDriver.class)
                .build();
        LOG.info("Robot: {} was created", robot);
    }
}
