package org.firstinspires.ftc.teamcode.Geometry;

import org.firstinspires.ftc.teamcode.Util.Vector;

public class Waypoint {
    public Vector coordinate;
    public double angle;
    public double velocity;

    public Waypoint(Vector pos, double theta, double v) {
        coordinate = pos;
        angle = theta;
        velocity = v;
    }
}
