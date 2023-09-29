package org.firstinspires.ftc.teamcode.Geometry;

import org.firstinspires.ftc.teamcode.Util.Vector;

public class Pose { // TODO: Velocity
    public Vector location;
    public double angle;
    public Pose(Vector location, double angle) {
        this.location = location;
        this.angle = angle;
    }
}
