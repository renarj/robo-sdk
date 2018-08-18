package com.oberasoftware.home.core.mqtt;

import com.google.common.util.concurrent.Uninterruptibles;
import com.oberasoftware.base.event.DistributedTopicEventBus;
import com.oberasoftware.base.event.Event;
import com.oberasoftware.base.event.EventFilter;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.impl.LocalEventBus;
import com.oberasoftware.robo.api.converters.ConverterManager;
import com.oberasoftware.robo.api.exceptions.ConversionException;
import com.oberasoftware.robo.api.exceptions.HomeAutomationException;
import com.oberasoftware.robo.api.exceptions.RuntimeHomeAutomationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Renze de Vries
 */
@Component
public class MQTTTopicEventBus implements DistributedTopicEventBus {
    private static final Logger LOG = LoggerFactory.getLogger(MQTTTopicEventBus.class);

    private static final String CONNECTION_STRING = "tcp://%s:%d";

    @Value("${mqtthost:}")
    private String mqttHost;

    @Value("${mqttport:1883}")
    private int mqttPort;

    @Value("${mqtt.username:}")
    private String mqttUsername;

    @Value("${mqtt.password:}")
    private String mqttPassword;

    @Value("${retryOnConnect:true}")
    private boolean retryOnConnect;

    @Autowired
    private ConverterManager convertManager;

    private MQTTBroker broker;

    @Autowired
    private LocalEventBus localEventBus;

    @PostConstruct
    public void initialize() throws HomeAutomationException {
        String connectionString = String.format(CONNECTION_STRING, mqttHost, mqttPort);
        broker = new MQTTBroker(connectionString, mqttUsername, mqttPassword);
        localEventBus.registerFilter(new MQTTPathFilter());
    }

    @Override
    public void subscribe(String topic) {
        LOG.info("Subscribing to topic: {}", topic);
        broker.subscribeTopic(topic);
    }

    @Override
    public List<String> getSubscriptions() {
        return null;
    }

    @Override
    public void unsubscribe(String s) {

    }

    @Override
    public synchronized void connect() {
        if(!StringUtils.isEmpty(mqttHost)) {
            if(!broker.isConnected()) {
                boolean connected = false;
                while(!connected) {
                    try {
                        connectBroker();
                        connected = true;
                    } catch (HomeAutomationException e) {
                        if(!retryOnConnect) {
                            throw new RuntimeHomeAutomationException("Unable to connect to MQTT Broker", e);
                        } else {
                            Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
                            LOG.warn("Could not connect, retrying in 5 seconds");
                        }
                    }
                }
            } else {
                LOG.warn("Already connected to broker");
            }
        } else {
            LOG.error("Cannot connect to MQTT host, not configured");
        }
    }

    private void connectBroker() throws HomeAutomationException {
        broker.connect();
        LOG.info("Connected to MQTT Broker: {} with user: {}", mqttHost, mqttUsername);

        broker.addListener((receivedTopic, payload) -> {
            LOG.debug("Received a message on topic: {} with payload: {}", receivedTopic, payload);

            localEventBus.publish(new MQTTMessageImpl(receivedTopic, payload));
        });
    }

    @Override
    public void disconnect() {
        if(broker != null) {
            broker.disconnect();
        }
    }

    @Override
    public void publish(Event event, Object... objects) {
        LOG.debug("Incoming event: {}", event);
        MQTTMessage message = convertManager.convert(event, MQTTMessage.class);
        if(message != null) {
            LOG.debug("Converted to MQTT message: {}", message);
            broker.publish(message);
        } else {
            throw new ConversionException("Unable to convert event: " + event);
        }
    }

    @Override
    public boolean publishSync(Event event, TimeUnit unit, long time, Object... arguments) {
        return false;
    }

    @Override
    public void registerHandler(EventHandler eventHandler) {
        localEventBus.registerHandler(eventHandler);
    }

    @Override
    public void registerFilter(EventFilter eventFilter) {

    }
}
