package com.oberasoftware.robo.api.behavioural;

import com.oberasoftware.robo.api.commands.Scale;

/**
 * @author renarj
 */
public interface ServoBehaviour extends Behaviour {

    Scale DEFAULT_SPEED_SCALE = new Scale(-100, 100);

    Scale DEFAULT_POSITION_SCALE = new Scale(0, 2000);

    void goToPosition(int percentage);

    void goToMinimum();

    void goToMaximum();

    void goToDefault();
}
