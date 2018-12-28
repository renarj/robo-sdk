package com.oberasoftware.robo.dynamixel.web.lidar;

public class LaserScan {
    private Header header;

    private double angle_min;
    private double angle_max;
    private double angle_increment;
    private double time_increment;
    private double scan_time;
    private double range_min;
    private double range_max;
    private double[] ranges = new double[0];
    private double[] intensities = new double[0];



    public LaserScan(double angle_min, double angle_max, double angle_increment, double time_increment, double scan_time, double range_min, double range_max, double[] ranges, double[] intensities, Header header) {
        this.angle_min = angle_min;
        this.angle_max = angle_max;
        this.angle_increment = angle_increment;
        this.time_increment = time_increment;
        this.scan_time = scan_time;
        this.range_min = range_min;
        this.range_max = range_max;
        this.ranges = ranges;
        this.intensities = intensities;
        this.header = header;
    }

    public double getAngle_min() {
        return angle_min;
    }

    public void setAngle_min(double angle_min) {
        this.angle_min = angle_min;
    }

    public double getAngle_max() {
        return angle_max;
    }

    public void setAngle_max(double angle_max) {
        this.angle_max = angle_max;
    }

    public double getAngle_increment() {
        return angle_increment;
    }

    public void setAngle_increment(double angle_increment) {
        this.angle_increment = angle_increment;
    }

    public double getTime_increment() {
        return time_increment;
    }

    public void setTime_increment(double time_increment) {
        this.time_increment = time_increment;
    }

    public double getScan_time() {
        return scan_time;
    }

    public void setScan_time(double scan_time) {
        this.scan_time = scan_time;
    }

    public double getRange_min() {
        return range_min;
    }

    public void setRange_min(double range_min) {
        this.range_min = range_min;
    }

    public double getRange_max() {
        return range_max;
    }

    public void setRange_max(double range_max) {
        this.range_max = range_max;
    }

    public double[] getRanges() {
        return ranges;
    }

    public void setRanges(double[] ranges) {
        this.ranges = ranges;
    }

    public double[] getIntensities() {
        return intensities;
    }

    public void setIntensities(double[] intensities) {
        this.intensities = intensities;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public static class Header {
        private int seq;
        private RosTime stamp;
        private String frame_id;

        public Header(int seq, RosTime stamp, String frame_id) {
            this.seq = seq;
            this.stamp = stamp;
            this.frame_id = frame_id;
        }

        public int getSeq() {
            return seq;
        }

        public void setSeq(int seq) {
            this.seq = seq;
        }

        public RosTime getStamp() {
            return stamp;
        }

        public void setStamp(RosTime stamp) {
            this.stamp = stamp;
        }

        public String getFrame_id() {
            return frame_id;
        }

        public void setFrame_id(String frame_id) {
            this.frame_id = frame_id;
        }
    }

    public static class RosTime {
        private long sec;
        private long nsec;

        public RosTime(long sec, long nsec) {
            this.sec = sec;
            this.nsec = nsec;
        }

        public long getSec() {
            return sec;
        }

        public void setSec(long sec) {
            this.sec = sec;
        }

        public long getNsec() {
            return nsec;
        }

        public void setNsec(long nsec) {
            this.nsec = nsec;
        }
    }
}
