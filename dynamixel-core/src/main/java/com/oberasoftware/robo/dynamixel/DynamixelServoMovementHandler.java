package com.oberasoftware.robo.dynamixel;

import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.robo.api.commands.PositionAndSpeedCommand;
import com.oberasoftware.robo.api.commands.PositionCommand;
import com.oberasoftware.robo.api.commands.SpeedCommand;

public interface DynamixelServoMovementHandler {
    @EventSubscribe
    void receive(PositionCommand positionCommand);

    @EventSubscribe
    void receive(SpeedCommand speedCommand);

    @EventSubscribe
    void receive(PositionAndSpeedCommand command);
}
