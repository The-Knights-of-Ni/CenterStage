package org.firstinspires.ftc.teamcode.Geometry;

import org.firstinspires.ftc.teamcode.Util.Vector;

public class Line {
    public Vector start;
    public Vector end;

    public Line(Vector start, Vector end) {
        this.start = start;
        this.end = end;
    }

    public double length() {
        return start.distance(end);
    }
}
