package com.oberasoftware.robo.dynamixel.web.test;

public class HexRobotBaseData {
    private final double coxaLength;
    private final double femurLength;
    private final double tibiaLength;

    private final double bodyLenght;
    private final double bodyWidth;
    private final double bodyWidthMiddle;

    public HexRobotBaseData(double coxaLength, double femurLength, double tibiaLength, double bodyLenght, double bodyWidth, double bodyWidthMiddle) {
        this.coxaLength = coxaLength;
        this.femurLength = femurLength;
        this.tibiaLength = tibiaLength;
        this.bodyLenght = bodyLenght;
        this.bodyWidth = bodyWidth;
        this.bodyWidthMiddle = bodyWidthMiddle;
    }

    public double getCoxaLength() {
        return coxaLength;
    }

    public double getFemurLength() {
        return femurLength;
    }

    public double getTibiaLength() {
        return tibiaLength;
    }

    public double getBodyLenght() {
        return bodyLenght;
    }

    public double getBodyWidth() {
        return bodyWidth;
    }

    public double getBodyWidthMiddle() {
        return bodyWidthMiddle;
    }

    @Override
    public String toString() {
        return "HexRobotBaseData{" +
                "coxaLength=" + coxaLength +
                ", femurLength=" + femurLength +
                ", tibiaLength=" + tibiaLength +
                ", bodyLenght=" + bodyLenght +
                ", bodyWidth=" + bodyWidth +
                ", bodyWidthMiddle=" + bodyWidthMiddle +
                '}';
    }
}
