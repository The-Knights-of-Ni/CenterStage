package org.firstinspires.ftc.teamcode.Util;

public class Pose {
    public double x;
    public double y;
    public double heading;
    public Vector velocity = new Vector(0, 0);

    public Pose(double x, double y, double heading) {
        this.x = x;
        this.y = y;
        this.heading = heading;
    }

    public Pose(Vector v, double heading) {
        this(v.getX(), v.getY(), heading);
    }

    public Vector getCoordinate() {
        return new Vector(x, y);
    }

    public boolean fuzzyCompare(Pose other, int distanceThreshold, int angleThreshold) {
        return Math.abs(x - other.x) < distanceThreshold && Math.abs(y - other.y) < distanceThreshold && Math.abs(heading - other.heading) < 5;
    }

    public boolean fuzzyCompare(Pose other) {
        return fuzzyCompare(other, 60, 5);
    }

    @Override
    public String toString() {
        return "Pose{" +
                "x=" + x +
                ", y=" + y +
                ", heading=" + heading +
                ", velocity=" + velocity +
                '}';
    }
}
