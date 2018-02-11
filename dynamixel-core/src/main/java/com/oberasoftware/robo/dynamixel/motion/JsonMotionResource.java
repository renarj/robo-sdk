package com.oberasoftware.robo.dynamixel.motion;

import com.oberasoftware.robo.api.motion.MotionResource;

/**
 * @author Renze de Vries
 */
public class JsonMotionResource implements MotionResource {
    private final String path;

    public JsonMotionResource(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "JsonMotionResource{" +
                "path='" + path + '\'' +
                '}';
    }
}
