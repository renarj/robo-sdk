package com.oberasoftware.robo.cloud;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.core.mqtt.MQTTTopicEventBus;
import com.oberasoftware.robo.api.CommandListener;
import com.oberasoftware.robo.api.RemoteDriver;
import com.oberasoftware.robo.api.commands.RobotCommand;
import com.oberasoftware.robo.api.events.RobotEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
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

    @PostConstruct
    public void connect() {
        LOG.info("Connecting to remote Robot Cloud");
        mqttTopicEventBus.connect();

        mqttTopicEventBus.subscribe("/commands");
        mqttTopicEventBus.registerHandler(this);
    }

    @Override
    public void publish(RobotEvent robotEvent) {
        LOG.info("Publishing robot event: {} to mqtt");
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
