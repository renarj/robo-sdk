package com.oberasoftware.robo.api.behavioural;


import com.oberasoftware.robo.api.Robot;

/**
 * @author renarj
 */
public interface Behaviour {
    void initialize(BehaviouralRobot behaviouralRobot, Robot robotCore);
}
