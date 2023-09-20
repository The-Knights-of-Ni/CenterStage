package org.firstinspires.ftc.teamcode.Merlin.Curve;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualNum;
import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualVector;
import org.firstinspires.ftc.teamcode.Merlin.PositionPath;
import org.firstinspires.ftc.teamcode.Util.Vector;

public class Line extends PositionPath<Arclength> {
    Vector begin;
    Vector dir;
    double length;

    /**
     * Makes line connecting [begin] to [end].
     */
    public Line(Vector begin, Vector end) {
        this.begin = begin;
        double norm = end.subtract(begin).getNorm();
        if (norm < 1e-6) {
            dir = new Vector(1.0, 0.0);
        } else {
            dir = new Vector(end.subtract(begin).scalarMultiply(1 / norm)); // TODO: Check if valid
        }
        length = end.subtract(begin).getNorm();
    }

    @Override
    public DualVector<Arclength> get(double param, int n) {
        return DualNum.<Arclength>variable(param, n).times(dir).plus(begin);
    }

    @Override
    public double length() {
        return length;
    }
}
