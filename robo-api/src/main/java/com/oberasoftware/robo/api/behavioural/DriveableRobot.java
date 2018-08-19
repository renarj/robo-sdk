package com.oberasoftware.robo.api.behavioural;

import java.util.Optional;

public interface DriveableRobot extends BehaviouralRobot {
    <T extends GripperBehaviour> Optional<T> getGripper();

    Optional<DriveBehaviour> getWheels();
}
