package com.oberasoftware.robo.dynamixel.web.test;

public class PosData {
    private double x;
    private double y;
    private double z;

    public PosData(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "PosData{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
