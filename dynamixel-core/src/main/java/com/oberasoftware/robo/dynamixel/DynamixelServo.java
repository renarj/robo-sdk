package com.oberasoftware.robo.dynamixel;

import com.oberasoftware.base.event.impl.LocalEventBus;
import com.oberasoftware.robo.api.commands.PositionCommand;
import com.oberasoftware.robo.api.commands.Scale;
import com.oberasoftware.robo.api.commands.SpeedCommand;
import com.oberasoftware.robo.api.commands.TorgueCommand;
import com.oberasoftware.robo.api.exceptions.ServoException;
import com.oberasoftware.robo.api.servo.Servo;
import com.oberasoftware.robo.api.servo.ServoData;
import com.oberasoftware.robo.api.servo.ServoDataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static java.lang.String.valueOf;

/**
 * @author Renze de Vries
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Component
@Scope("prototype")
public class DynamixelServo implements Servo {
    private static final Logger LOG = LoggerFactory.getLogger(DynamixelServo.class);

    private final int id;
    private final int deviceTypeId;

    @Autowired
    private LocalEventBus eventBus;

    @Autowired
    private ServoDataManager servoDataManager;

    public DynamixelServo(int id, int deviceTypeId) {
        this.id = id;
        this.deviceTypeId = deviceTypeId;
    }

    @Override
    public String getId() {
        return valueOf(id);
    }

    @Override
    public int getDeviceTypeId() {
        return deviceTypeId;
    }

    @Override
    public ServoData getData() {
        return new AsyncServoData(servoDataManager, getId());
    }

    @Override
    public void moveTo(int position, Scale scale) {
        if(!scale.isValid(position)) {
            throw new ServoException("Invalid range specific for goal position, only -180 to 180 supported was: " + position);
        } else {
            LOG.debug("Setting goal position to: {} for servo: {}", position, id);
            eventBus.publish(new PositionCommand(valueOf(id), position, scale));
        }
    }

    @Override
    public void setSpeed(int speed, Scale scale) {
        if(!scale.isValid(speed)) {
            throw new ServoException("Invalid speed, range needs to be between 0 and 1024, currently: " + speed);
        } else {
            LOG.debug("Setting target speed: {} for servo: {}", speed, id);
            eventBus.publish(new SpeedCommand(valueOf(id), speed, scale));
        }
    }

    @Override
    public void setTorgueLimit(int torgueLimit) {

    }

    @Override
    public void enableTorgue() {
        eventBus.publish(new TorgueCommand(valueOf(id), true));
    }

    @Override
    public void disableTorgue() {
        eventBus.publish(new TorgueCommand(valueOf(id), false));
    }
}
