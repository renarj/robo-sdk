package com.oberasoftware.robo.api.behavioural;


import java.util.List;
import java.util.Optional;

/**
 * @author renarj
 */
public interface BehaviouralRobot {
    String getRobotId();

    List<Behaviour> getBehaviours();

    <T extends Behaviour> T getBehaviour(Class<T> behaviourClass);

    <T extends GripperBehaviour> Optional<T> getGripper();

    Optional<DriveBehaviour> getWheels();
}
