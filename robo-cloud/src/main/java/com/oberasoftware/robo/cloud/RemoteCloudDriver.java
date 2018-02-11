package com.oberasoftware.robo.cloud;

import com.oberasoftware.base.event.Event;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.client.api.CommandServiceClient;
import com.oberasoftware.home.client.api.StateServiceClient;
import com.oberasoftware.robo.api.commands.BasicCommand;
import com.oberasoftware.home.core.mqtt.MQTTTopicEventBus;
import com.oberasoftware.robo.api.RemoteDriver;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.commands.CommandListener;
import com.oberasoftware.robo.api.commands.RobotCommand;
import com.oberasoftware.robo.cloud.handlers.RobotStateServiceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Renze de Vries
 */
@Component
@Scope("prototype")
public class RemoteCloudDriver implements RemoteDriver, EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RemoteCloudDriver.class);

    @Autowired
    private MQTTTopicEventBus mqttTopicEventBus;

    @Autowired
    private StateServiceClient stateServiceClient;

    @Autowired
    private CommandServiceClient commandServiceClient;

    @Autowired
    private RobotStateServiceListener stateServiceListener;

    @Autowired
    private KeepAliveHandler keepAliveHandler;

    private List<CommandListener> commandListeners = new CopyOnWriteArrayList<>();

    @Override
    public void activate(Robot robot, Map<String, String> properties) {
        LOG.info("Connecting to remote Robot Cloud");

        if (robot.isRemote()) {
            stateServiceClient.listen(stateServiceListener);
            stateServiceClient.connect();
        } else {
            LOG.info("Listening to remote commands");
            mqttTopicEventBus.connect();

            mqttTopicEventBus.registerHandler(this);
            mqttTopicEventBus.subscribe("/commands/" + robot.getName() + "/#");
            keepAliveHandler.start(robot);
        }
    }

    @Override
    public void shutdown() {
        LOG.info("Disconnecting from robot cloud");
        mqttTopicEventBus.disconnect();
    }

    @Override
    public void publish(Event robotEvent) {
        if(robotEvent instanceof BasicCommand) {
            LOG.debug("Publishing robot command: {} to command service", robotEvent);
            commandServiceClient.sendCommand((BasicCommand)robotEvent);
        } else {
            LOG.debug("Publishing robot event: {} to mqtt", robotEvent);
            mqttTopicEventBus.publish(robotEvent);
        }
    }

    @Override
    public void register(CommandListener<?> commandListener) {
        LOG.info("Registering command listener");

        commandListeners.add(commandListener);
    }

    @EventSubscribe
    public void receiveCommand(RobotCommand command) {
        LOG.debug("Received robot command: {} from cloud", command);
        commandListeners.forEach(l -> l.receive(command));
    }
}
