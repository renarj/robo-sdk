package com.oberasoftware.robo.api.motion;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface Motion {
    String getId();

    String getName();

    List<KeyFrame> getKeyFrames();

    String getNextMotion();

    String getExitMotion();

    int getRepeats();
}
