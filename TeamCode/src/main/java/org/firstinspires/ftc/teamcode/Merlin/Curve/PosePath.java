package org.firstinspires.ftc.teamcode.Merlin.Curve;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualPose;

public abstract class PosePath {
    public abstract DualPose<Arclength> get(double s, int n);

    public abstract double length();

    public DualPose<Arclength> begin(int n) {
        return get(0.0, n);
    }

    public DualPose<Arclength> end(int n) {
        return get(length(), n);
    }
}
