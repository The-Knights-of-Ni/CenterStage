package org.firstinspires.ftc.teamcode.Merlin.Curve;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualNum;

public class QuinticSpline1d {

    private double a;
    private double b;
    private double c;
    private double d;
    private double e;
    private double f;

    public QuinticSpline1d(DualNum<Internal> begin, DualNum<Internal> end) {
        a = -6.0 * begin.get(0) - 3.0 * begin.get(1) - 0.5 * begin.get(2) +
                6.0 * end.get(0) - 3.0 * end.get(1) + 0.5 * end.get(2);
        b = 15.0 * begin.get(0) + 8.0 * begin.get(1) + 1.5 * begin.get(2) -
                15.0 * end.get(0) + 7.0 * end.get(1) - end.get(2);
        c = -10.0 * begin.get(0) - 6.0 * begin.get(1) - 1.5 * begin.get(2) +
                10.0 * end.get(0) - 4.0 * end.get(1) + 0.5 * end.get(2);
        d = 0.5 * begin.get(2);
        e = begin.get(1);
        f = begin.get(0);
    }

    public DualNum<Internal> get(double t, int n) {
        double[] values = new double[n];
        for (int i = 0; i < n; i++) {
            switch (i) {
                case 0:
                    values[i] = ((((a * t + b) * t + c) * t + d) * t + e) * t + f;
                    break;
                case 1:
                    values[i] = (((5.0 * a * t + 4.0 * b) * t + 3.0 * c) * t + 2.0 * d) * t + e;
                case 2:
                    values[i] = ((20.0 * a * t + 12.0 * b) * t + 6.0 * c) * t + 2.0 * d;
                case 3:
                    values[i] = (60.0 * a * t + 24.0 * b) * t + 6.0 * c;
                case 4:
                    values[i] = 120.0 * a * t + 24.0 * b;
                case 5:
                    values[i] = 120.0 * a;
                default:
                    values[i] = 0.0;
                    break;
            }
        }
        return new DualNum<>(values);
    }
}
