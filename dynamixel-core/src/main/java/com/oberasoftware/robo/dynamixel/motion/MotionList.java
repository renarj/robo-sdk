package com.oberasoftware.robo.dynamixel.motion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.oberasoftware.robo.api.motion.Motion;
import com.oberasoftware.robo.core.motion.MotionImpl;

import java.util.List;

/**
 * @author Renze de Vries
 */
public class MotionList {

    @JsonDeserialize(contentAs=MotionImpl.class)
    private List<Motion> motions;

    public MotionList() {
    }

    public List<Motion> getMotions() {
        return motions;
    }

    public void setMotions(List<Motion> motions) {
        this.motions = motions;
    }
}
