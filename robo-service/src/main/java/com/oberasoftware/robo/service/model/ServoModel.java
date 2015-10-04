package com.oberasoftware.robo.service.model;

import com.sdl.odata.api.edm.annotations.EdmEntity;
import com.sdl.odata.api.edm.annotations.EdmEntitySet;
import com.sdl.odata.api.edm.annotations.EdmProperty;

/**
 * @author Renze de Vries
 */
@EdmEntity(name = "Servo", namespace = "Oberasoftware.Robot", key = "id", containerName = "Robots")
@EdmEntitySet(name = "Servos")
public class ServoModel {

    @EdmProperty(name = "id", nullable = false)
    private String id;

    @EdmProperty(name = "speed", nullable = false)
    private int speed;

    @EdmProperty(name = "position", nullable = false)
    private int position;

    public ServoModel(String id, int speed, int position) {
        this.id = id;
        this.speed = speed;
        this.position = position;
    }

    public ServoModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "ServoModel{" +
                "id='" + id + '\'' +
                ", speed=" + speed +
                ", position=" + position +
                '}';
    }
}
