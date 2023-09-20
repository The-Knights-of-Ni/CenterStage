package org.firstinspires.ftc.teamcode.Merlin;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualVector;

public abstract class PositionPath<Param> {

    public abstract DualVector<Param> get(double param, int n);

    public abstract double length();

    public DualVector<Param> begin(int n) {
        return get(0.0, n);
    }

    public DualVector<Param> end(int n) {
        return get(length(), n);
    }
}
