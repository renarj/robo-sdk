package com.oberasoftware.robo.dynamixel.web.test;

import com.oberasoftware.robo.api.servo.Servo;

public class Leg {
    private final String legId;

    private final double offsetX;
    private final double offsetY;

    private final Servo coxia;
    private final Servo femur;
    private final Servo tibia;

    private final double coxaOffset;

    private final PosData initialPosition;

    private final int dirMod;

    public Leg(String legId, double offsetX, double offsetY, PosData initialPosition, Servo coxia, Servo femur, Servo tibia, double coxaOffset, int dirMod) {
        this.legId = legId;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.coxia = coxia;
        this.femur = femur;
        this.tibia = tibia;
        this.initialPosition = initialPosition;
        this.coxaOffset = coxaOffset;
        this.dirMod = dirMod;
    }

    public int getDirMod() {
        return dirMod;
    }

    public String getLegId() {
        return legId;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public Servo getCoxia() {
        return coxia;
    }

    public Servo getFemur() {
        return femur;
    }

    public Servo getTibia() {
        return tibia;
    }

    public PosData getInitialPosition() {
        return initialPosition;
    }

    public double getCoxaOffset() {
        return coxaOffset;
    }

    @Override
    public String toString() {
        return "Leg{" +
                "legId='" + legId + '\'' +
                ", offsetX=" + offsetX +
                ", offsetY=" + offsetY +
                ", coxia=" + coxia +
                ", femur=" + femur +
                ", tibia=" + tibia +
                ", coxaOffset=" + coxaOffset +
                ", initialPosition=" + initialPosition +
                '}';
    }
}
