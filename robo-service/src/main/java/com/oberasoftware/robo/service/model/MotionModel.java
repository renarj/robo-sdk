package com.oberasoftware.robo.service.model;

import com.oberasoftware.robo.service.QueryableEntity;
import com.sdl.odata.api.edm.annotations.EdmEntity;
import com.sdl.odata.api.edm.annotations.EdmEntitySet;
import com.sdl.odata.api.edm.annotations.EdmProperty;

/**
 * @author Renze de Vries
 */
@EdmEntity(name = "Motion", namespace = "Oberasoftware.Robot", key = "id", containerName = "Robots")
@EdmEntitySet(name = "Motions")
public class MotionModel implements QueryableEntity {
    @EdmProperty(name = "id", nullable = false)
    private String id;

    public MotionModel(String id) {
        this.id = id;
    }

    public MotionModel() {
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getProperty(String property) {
        return property.equals("id") ? id : null;
    }

    @Override
    public String toString() {
        return "MotionModel{" +
                "id='" + id + '\'' +
                '}';
    }
}
