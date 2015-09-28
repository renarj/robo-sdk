package com.oberasoftware.robo.dynamixel;

import com.google.common.util.concurrent.Uninterruptibles;
import com.oberasoftware.base.BaseConfiguration;
import com.oberasoftware.robo.api.RobotController;
import com.oberasoftware.robo.api.Servo;
import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.dynamixel.robomotion.MotionConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Renze de Vries
 */
@SpringBootApplication
@Import({DynamixelConfiguration.class, BaseConfiguration.class})
public class DynamixelTest {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelTest.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DynamixelTest.class, args);

        RobotController controller = context.getBean(RobotController.class);
        MotionConverter motionConverter = context.getBean(MotionConverter.class);
        List<Motion> motions = motionConverter.loadMotions("/bio_prm_kingspider_en.mtn");
        Map<String, Motion> motionMap = motions.stream().collect(Collectors.toMap(Motion::getName, m -> m));

        boolean s = controller.initialize();
        if(s) {
            LOG.info("Controller succesfully initialized");
        } else {
            LOG.error("Controller could not initialize");
            System.exit(-1);
        }

        List<Servo> servoList = controller.getServos();
        LOG.debug("Found servos: {}", servoList);

        LOG.debug("Enabling torgue");
        servoList.forEach(Servo::enableTorgue);

        LOG.debug("Readying the robot");
        Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
        controller.executeMotion(motionMap.get("Ready"));

        LOG.debug("Doing a short walk in a bit");
        Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
        controller.executeMotion(motionMap.get("Forward walk"), 4);

        Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
        controller.executeMotion(motionMap.get("Ready"));

        LOG.debug("Waiting for program end");
        Uninterruptibles.sleepUninterruptibly(100, TimeUnit.SECONDS);


        System.exit(0);
    }
}
