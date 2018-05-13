package com.oberasoftware.robo.api.behavioural;

/**
 * @author renarj
 */
public interface RotatableGripper extends GripperBehaviour {
    void rotate(int degrees);

    void rotateLevel();
}
