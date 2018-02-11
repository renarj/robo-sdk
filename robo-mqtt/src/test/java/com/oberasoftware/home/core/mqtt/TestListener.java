package com.oberasoftware.home.core.mqtt;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

import static com.google.common.util.concurrent.Uninterruptibles.awaitUninterruptibly;

/**
 * @author Renze de Vries
 */
@Component
public class TestListener implements EventHandler {

    private String lastTopic = null;
    private String lastMessage = null;

    private CountDownLatch latch = new CountDownLatch(1);

    @EventSubscribe
    public void receive(MQTTMessage mqttMessage) {
        this.lastTopic = mqttMessage.getTopic();
        this.lastMessage = mqttMessage.getMessage();

        latch.countDown();
    }

    public String getLastTopic() {
        return lastTopic;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void await() {
        awaitUninterruptibly(latch);
    }
}
