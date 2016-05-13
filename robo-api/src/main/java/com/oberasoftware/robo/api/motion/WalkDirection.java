package com.oberasoftware.robo.api.motion;

/**
 * @author Renze de Vries
 */
public enum WalkDirection {
    FORWARD,
    BACKWARD,
    LEFT,
    RIGHT,
    STOP;

    public static WalkDirection fromString(String direction) {
        for(WalkDirection v: values()) {
            if(v.name().equalsIgnoreCase(direction)) {
                return v;
            }
        }

        return FORWARD;
    }
}
