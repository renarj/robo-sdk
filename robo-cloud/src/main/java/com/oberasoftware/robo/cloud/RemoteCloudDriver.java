package com.oberasoftware.robo.cloud;

import com.oberasoftware.base.event.Event;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.core.mqtt.MQTTTopicEventBus;
import com.oberasoftware.robo.api.RemoteDriver;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.commands.CommandListener;
import com.oberasoftware.robo.api.commands.RobotCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Renze de Vries
 */
@Component
public class RemoteCloudDriver implements RemoteDriver, EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RemoteCloudDriver.class);

    @Autowired
    private MQTTTopicEventBus mqttTopicEventBus;

    private List<CommandListener> commandListeners = new CopyOnWriteArrayList<>();

    @Override
    public void activate(Robot robot, Map<String, String> properties) {
        LOG.info("Connecting to remote Robot Cloud");
        mqttTopicEventBus.connect();

        if(properties.containsKey("listen")) {
            LOG.info("Listening to remote commands");
            mqttTopicEventBus.registerHandler(this);
            mqttTopicEventBus.subscribe("/commands/" + robot.getName() + "/#");
        } else {
            mqttTopicEventBus.registerHandler(this);
            mqttTopicEventBus.subscribe("/states/" + robot.getName() + "/#");
        }
    }

    @Override
    public void shutdown() {
        LOG.info("Disconnecting from robot cloud");
        mqttTopicEventBus.disconnect();
    }

    @Override
    public void publish(Event robotEvent) {
        LOG.info("Publishing robot event: {} to mqtt", robotEvent);
        mqttTopicEventBus.publish(robotEvent);
    }

    @Override
    public void register(CommandListener<?> commandListener) {
        LOG.info("Registering command listener");

        commandListeners.add(commandListener);
    }

    @EventSubscribe
    public void receiveCommand(RobotCommand command) {
        LOG.info("Received robot command: {} from cloud", command);
        commandListeners.forEach(l -> l.receive(command));
    }
}
