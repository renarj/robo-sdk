package com.oberasoftware.robo.cloud.handlers;

import com.oberasoftware.home.client.api.StateServiceListener;
import com.oberasoftware.robo.core.model.ValueTransportMessage;
import com.oberasoftware.robo.api.Robot;
import com.oberasoftware.robo.api.RobotRegistry;
import com.oberasoftware.robo.api.events.ValueEventImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Renze de Vries
 */
@Component
public class RobotStateServiceListener implements StateServiceListener {
    private static final Logger LOG = LoggerFactory.getLogger(RobotStateServiceListener.class);

    @Autowired
    private RobotRegistry robotRegistry;

    @Override
    public void receive(ValueTransportMessage message) {
        LOG.debug("Received state message: {}", message);
        Robot robot = robotRegistry.getRobot(message.getControllerId());
        robot.publish(new ValueEventImpl(message.getControllerId(), message.getChannelId(), message.getLabel(), message.getValue()));
    }
}
