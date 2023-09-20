package org.firstinspires.ftc.teamcode.Merlin.Curve;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualVector;
import org.firstinspires.ftc.teamcode.Merlin.PositionPath;

public class QuinticSpline2d extends PositionPath<Internal> {
    QuinticSpline1d x;
    QuinticSpline1d y;
    public QuinticSpline2d(DualVector<Internal> begin, DualVector<Internal> end) {
        x = new QuinticSpline1d(begin.getX(), end.getX());
        y = new QuinticSpline1d(begin.getY(), end.getY());
    }

    @Override
    public DualVector<Internal> get(double param, int n) {
        return new DualVector<>(x.get(param, n), y.get(param, n));
    }

    @Override
    public double length() {
        return 1.0;
    }
}
