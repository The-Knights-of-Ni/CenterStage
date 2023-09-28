package org.firstinspires.ftc.teamcode.Merlin.Curve;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualVector;
import org.firstinspires.ftc.teamcode.Merlin.PositionPath;

public class PositionPathView<Param> extends PositionPath<Param> {
    PositionPath<Param> path;
    double offset;
    double length;

    public PositionPathView(PositionPath<Param> path, double offset, double length) {
        this.path = path;
        this.offset = offset;
        this.length = length;
    }

    @Override
    public DualVector<Param> get(double param, int n) {
        return path.get(param + offset, n);
    }

    @Override
    public double length() {
        return length;
    }
}
