package com.oberasoftware.home.client.state;

import com.oberasoftware.home.client.api.StateServiceClient;
import com.oberasoftware.home.client.api.StateServiceListener;
import com.oberasoftware.robo.core.model.ValueTransportMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Renze de Vries
 */
@Component
public class StateServiceClientImpl implements StateServiceClient {
    private static final Logger LOG = LoggerFactory.getLogger(StateServiceClientImpl.class);

    @Value("${state_svc_url:}")
    private String stateServiceUrl;

    private WebSocketStompClient stompClient;

    private List<StateServiceListener> listeners = new CopyOnWriteArrayList<>();

    @Override
    public void connect() {
        if(!StringUtils.isEmpty(stateServiceUrl)) {
            LOG.info("Connecting to State websocket endpoint: {}", getStateServiceUrl());
            List<Transport> transports = new ArrayList<>(1);
            transports.add(new WebSocketTransport( new StandardWebSocketClient()));
            WebSocketClient transport = new SockJsClient(transports);

            stompClient = new WebSocketStompClient(transport);
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());

            StompSessionHandler sessionHandler = new StateServiceSessionHandler();
            stompClient.connect(getStateServiceUrl(), sessionHandler);
        } else {
            LOG.error("Cannot connect to the state service, no endpoint configured");
        }
    }

    @Override
    public void disconnect() {
        if(stompClient != null) {
            LOG.info("Disconnect websocket client");
            stompClient.stop();
        }
    }

    @Override
    public void listen(StateServiceListener listener) {
        this.listeners.add(listener);
    }

    private String getStateServiceUrl() {
        String url = stateServiceUrl;
        if(!url.endsWith("/")) {
            url += "/";
        }
        return url + "ws";
    }

    private class StateServiceSessionHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            LOG.info("Connected to state service");
            session.subscribe("/topic/state", new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders stompHeaders) {
                    return ValueTransportMessage.class;
                }

                @Override
                public void handleFrame(StompHeaders stompHeaders, Object o) {
                    LOG.debug("Received a state message: {}", o);
                    listeners.forEach(l -> l.receive((ValueTransportMessage) o));
                }
            });
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            LOG.error("StateService communication error: {} content: {}", exception.getMessage(), new String(payload));
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            LOG.error("StateService Transport error", exception);
        }
    }
}
