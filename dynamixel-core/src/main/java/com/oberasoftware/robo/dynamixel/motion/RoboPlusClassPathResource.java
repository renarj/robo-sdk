package com.oberasoftware.robo.dynamixel.motion;


import com.oberasoftware.robo.api.motion.MotionResource;

/**
 * @author Renze de Vries
 */
public class RoboPlusClassPathResource implements MotionResource {
    private final String path;

    public RoboPlusClassPathResource(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "RoboPlusClassPathResource{" +
                "path='" + path + '\'' +
                '}';
    }
}
