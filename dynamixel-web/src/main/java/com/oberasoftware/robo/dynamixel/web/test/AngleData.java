package com.oberasoftware.robo.dynamixel.web.test;

public class AngleData {
    private final double coxaAngle;
    private final double femurAngle;
    private final double tibiaAngle;

    public AngleData(double coxaAngle, double femurAngle, double tibiaAngle) {
        this.coxaAngle = coxaAngle;
        this.femurAngle = femurAngle;
        this.tibiaAngle = tibiaAngle;
    }

    public double getCoxaAngle() {
        return coxaAngle;
    }

    public double getFemurAngle() {
        return femurAngle;
    }

    public double getTibiaAngle() {
        return tibiaAngle;
    }

    @Override
    public String toString() {
        return "AngleData{" +
                "coxaAngle=" + coxaAngle +
                ", femurAngle=" + femurAngle +
                ", tibiaAngle=" + tibiaAngle +
                '}';
    }
}
