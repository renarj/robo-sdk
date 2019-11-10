package com.oberasoftware.robo.api.motion;

import java.util.List;

/**
 * @author Renze de Vries
 */
public interface Motion {
    String getName();

    List<KeyFrame> getKeyFrames();
}
