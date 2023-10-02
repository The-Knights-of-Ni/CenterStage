package org.firstinspires.ftc.teamcode.Geometry;

import org.firstinspires.ftc.teamcode.Util.Pose;

public class Waypoint {
    public Pose pose;
    public double velocity;

    public Waypoint(Pose pose, double v) {
        this.pose = pose;
        velocity = v;
    }
}
