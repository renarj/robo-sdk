package com.oberasoftware.robo.cloud;

import com.google.common.util.concurrent.Uninterruptibles;
import com.oberasoftware.home.core.mqtt.MQTTTopicEventBus;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.commands.PingCommand;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class KeepAliveHandler implements Runnable {
    private static final Logger LOG = getLogger(KeepAliveHandler.class);

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    private MQTTTopicEventBus mqttTopicEventBus;

    private Robot robot;


    public void start(Robot robot) {
        this.robot = robot;

        LOG.info("Starting keep alive thread");
        executorService.submit(this);
    }

    public void stop() {
        executorService.shutdown();
        try {
            LOG.info("Keep Alive stopped: {}", executorService.awaitTermination(10, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            LOG.error("Could not stop keep alive thread cleanly", e);
        }
    }

    @Override
    public void run() {
        while(Thread.currentThread().isInterrupted()) {
            mqttTopicEventBus.publish(new PingCommand(robot.getName()));
//            mqttTopicEventBus.publish(BasicCommandBuilder
//                    .create(robot.getName())
//                    .item("data")
//                    .label("ping")
//                    .build());

            Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
        }
        LOG.info("Keep Alive stopped");
    }
}
