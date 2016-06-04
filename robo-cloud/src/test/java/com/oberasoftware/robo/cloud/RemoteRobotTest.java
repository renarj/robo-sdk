package com.oberasoftware.robo.cloud;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.events.ValueEvent;
import com.oberasoftware.robo.core.CoreConfiguration;
import com.oberasoftware.robo.core.SpringAwareRobotBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

/**
 * @author Renze de Vries
 */
@SpringBootApplication
@Import({
        RemoteConfiguration.class,
        CoreConfiguration.class
})
public class RemoteRobotTest {
    private static final Logger LOG = LoggerFactory.getLogger(RemoteRobotTest.class);

    public static void main(String[] args) {
        LOG.info("Starting hybrid robot test");

        ApplicationContext context = new SpringApplication(RemoteRobotTest.class).run(args);

        Robot max = new SpringAwareRobotBuilder("max", context)
                .motionEngine(RemoteMotionEngine.class)
                .remote(RemoteCloudDriver.class)
                .build();
        MaxRobotEventHandler eventHandler = new MaxRobotEventHandler(max);
        max.listen(eventHandler);

        max.getMotionEngine().prepareWalk();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Killing the robot gracefully on shutdown");
            max.shutdown();
        }));

    }

    private static class MaxRobotEventHandler implements EventHandler {

        private Robot max;

        private MaxRobotEventHandler(Robot max) {
            this.max = max;
        }

        @EventSubscribe
        public void receive(ValueEvent valueEvent) {
            if(valueEvent.getControllerId().equals("max") && valueEvent.getLabel().equals("distance")) {
                int distance = valueEvent.getValue().getValue();
                if(distance < 20) {
                    LOG.info("Distance is too small: {}", distance);
                }
            }
        }
    }
}
