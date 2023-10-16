package org.firstinspires.ftc.teamcode.Subsystems.Drive.Curve;

import org.firstinspires.ftc.teamcode.Util.Vector;

public class QuinticSpline2D {
    public QuinticSpline1D x;
    public QuinticSpline1D y;

    public QuinticSpline2D(QuinticSpline1D x, QuinticSpline1D y) {
        this.x = x;
        this.y = y;
    }

    public Vector[] get(double t, int n) {
        double[] xG = x.get(t, n);
        double[] yG = y.get(t, n);
        Vector[] result = new Vector[n];
        for (int i = 0; i < n; i++) {
            result[i] = new Vector(xG[i], yG[i]);
        }
        return result;
    }

    @Override
    public String toString() {
        return "QuinticSpline2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
