package org.firstinspires.ftc.teamcode.Merlin.Curve;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualRotation;

public abstract class HeadingPath {
    abstract DualRotation<Arclength> get(double s, int n);

    abstract double length();

    public DualRotation<Arclength> begin(int n) {
        return get(0.0, n);
    }

    public DualRotation<Arclength> end(int n) {
        return get(length(), n);
    }
}