package com.oberasoftware.robo.core;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.*;
import com.oberasoftware.robo.api.commands.CommandListener;
import com.oberasoftware.robo.api.commands.RobotCommand;
import com.oberasoftware.robo.api.events.RobotEvent;
import com.oberasoftware.robo.api.servo.ServoDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Renze de Vries
 */
public class RemoteEnabledRobot implements Robot, CommandListener, EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RemoteEnabledRobot.class);

    private final Robot localRobot;
    private final RemoteDriver remoteDriver;

    public RemoteEnabledRobot(RemoteDriver remoteDriver, Robot localRobot) {
        this.localRobot = localRobot;
        this.remoteDriver = remoteDriver;
        remoteDriver.register(this);
        this.localRobot.listen(this);
    }

    @Override
    public String getName() {
        return localRobot.getName();
    }

    @Override
    public void listen(EventHandler robotEventHandler) {
        this.localRobot.listen(robotEventHandler);
    }

    @Override
    public MotionEngine getMotionEngine() {
        return localRobot.getMotionEngine();
    }

    @Override
    public ServoDriver getServoDriver() {
        return localRobot.getServoDriver();
    }

    @Override
    public List<Capability> getCapabilities() {
        return localRobot.getCapabilities();
    }

    @Override
    public <T extends Capability> T getCapability(Class<T> capabilityClass) {
        return localRobot.getCapability(capabilityClass);
    }

    @Override
    public RemoteDriver getRemoteDriver() {
        return remoteDriver;
    }

    @Override
    public void shutdown() {
        this.localRobot.shutdown();
    }

    @Override
    public void receive(RobotCommand command) {
        LOG.info("Received a remote robot command: {}", command);
    }

    @EventSubscribe
    public void receiveRobotEvent(RobotEvent robotEvent) {
        LOG.info("Received a robot event: {}", robotEvent);
        remoteDriver.publish(robotEvent);
    }
}
