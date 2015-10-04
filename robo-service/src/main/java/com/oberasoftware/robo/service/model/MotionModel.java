package com.oberasoftware.robo.service.model;

import com.sdl.odata.api.edm.annotations.EdmEntity;
import com.sdl.odata.api.edm.annotations.EdmEntitySet;
import com.sdl.odata.api.edm.annotations.EdmProperty;

/**
 * @author Renze de Vries
 */
@EdmEntity(name = "Motion", namespace = "Oberasoftware.Robot", key = "id", containerName = "Robots")
@EdmEntitySet(name = "Motions")
public class MotionModel {
    @EdmProperty(name = "name", nullable = false)
    private String name;

    public MotionModel(String name) {
        this.name = name;
    }

    public MotionModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MotionModel{" +
                "name='" + name + '\'' +
                '}';
    }
}
