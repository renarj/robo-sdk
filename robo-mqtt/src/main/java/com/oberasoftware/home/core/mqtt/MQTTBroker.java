package com.oberasoftware.home.core.mqtt;

import com.oberasoftware.robo.api.exceptions.HomeAutomationException;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Renze de Vries
 */
public class MQTTBroker {
    private static final Logger LOG = LoggerFactory.getLogger(MQTTBroker.class);

    private final String host;
    private String username = null;
    private String password = null;

    private MqttClient client;
    private final AtomicBoolean connected = new AtomicBoolean(false);

    private List<MQTTListener> listeners = new CopyOnWriteArrayList<>();

    public MQTTBroker(String host) {
        this.host = host;
    }

    public MQTTBroker(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public synchronized void connect() throws HomeAutomationException {
        try {
            LOG.info("Connecting to host: {}", host);
            client = new MqttClient(host, UUID.randomUUID().toString());
            client.setTimeToWait(5000);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    LOG.warn("Connection lost to host: {}", host);
                    try {
                        client.reconnect();
                    } catch (MqttException e) {
                        LOG.error("Could not reconnect", e);
                    }
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    LOG.debug("Message arrived: {}", s);
                    listeners.forEach(l -> l.receive(s, new String(mqttMessage.getPayload())));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    LOG.debug("delivery complete");
                }
            });
            MqttConnectOptions options = new MqttConnectOptions();
            options.setKeepAliveInterval(20);
            options.setMaxInflight(10000);
            options.setAutomaticReconnect(true);
            options.setConnectionTimeout(5);

            if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
                LOG.info("Authentication details specified, using for connection");
                options.setUserName(username);
                options.setPassword(password.toCharArray());
            } else {
                LOG.info("No authentication details, using anonymous connection");
            }
            client.connect(options);
            connected.set(true);
        } catch (MqttException e) {
            throw new HomeAutomationException("Could not connect to MQTT broker: " + host);
        }
    }

    public boolean isConnected() {
        return connected.get();
    }

    public void disconnect() {
        if(connected.get()) {
            try {
                client.disconnect();
            } catch (MqttException e) {
                LOG.error("Could not safely disconnect from MQTT broker: " + host);
            }
        }
    }

    public void addListener(MQTTListener listener) {
        listeners.add(listener);
    }

    public void subscribeTopic(String topic) {
        if(connected.get()) {
            try {
                client.subscribe(topic, 1);
            } catch (MqttException e) {
                LOG.error("Could not subscribe to topic: " + topic, e);
            }
        }
    }

    public void publish(MQTTMessage message) {
        if(connected.get()) {
            try {
                MqttMessage m = new MqttMessage(message.getMessage().getBytes());
                m.setQos(1);

                client.publish(message.getTopic(), m);
            } catch (MqttException e) {
                LOG.error("Could not publish message: " + message, e);
            }
        }
    }
}
