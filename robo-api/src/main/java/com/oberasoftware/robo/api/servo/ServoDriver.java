package com.oberasoftware.robo.api.servo;

import com.oberasoftware.robo.api.ActivatableCapability;
import com.oberasoftware.robo.api.commands.PositionAndSpeedCommand;

import java.util.List;
import java.util.Map;

/**
 * @author Renze de Vries
 */
public interface ServoDriver extends ActivatableCapability {
    boolean setServoSpeed(String servoId, int speed);

    boolean setTargetPosition(String servoId, int targetPosition);

    boolean setPositionAndSpeed(String servoId, int speed, int targetPosition);

    boolean bulkSetPositionAndSpeed(Map<String, PositionAndSpeedCommand> commands);

    boolean supportsCommand(ServoCommand servoCommand);

    boolean sendCommand(ServoCommand servoCommand);

    boolean setTorgue(String servoId, int limit);

    boolean setTorgue(String servoId, boolean state);

    List<Servo> getServos();

    Servo getServo(String servoId);
}
