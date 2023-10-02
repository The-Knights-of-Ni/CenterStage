package org.firstinspires.ftc.teamcode.Util;

public class Pose {
    public int x;
    public int y;
    public int heading;

    public Pose(int x, int y, int heading) {
        this.x = x;
        this.y = y;
        this.heading = heading;
    }

    public Pose(Vector v, int heading) {
        this((int) v.getX(), (int) v.getY(), heading);
    }

    public Vector getCoordinate() {
        return new Vector(x, y);
    }

    public boolean fuzzyCompare(Pose other) { // TODO: Make more configurable (e.g. modifiable thresholds)
        return Math.abs(x - other.x) < 60 && Math.abs(y - other.y) < 60 && Math.abs(heading - other.heading) < 5;
    }
}
