package org.firstinspires.ftc.teamcode.Subsystems.Drive.Curve;


public class QuinticSpline1D {
    double a;
    double b;
    double c;
    double d;
    double e;
    double f;

    public QuinticSpline1D(double[] begin, double[] end) {
        if (begin.length <= 3 || end.length <= 3) {
            throw new IllegalArgumentException("begin and end must have at least 3 elements");
        }
        this.a = -6.0 * begin[0] - 3.0 * begin[1] + 1.5 * begin[2] - 15.0 * end[0] - 3.0 * end[1] + 0.5 * end[2];
        this.b = 15.0 * begin[0] + 8.0 * begin[1] + 1.5 * begin[2] - 15.0 * end[0] + 7.0 * end[1] - end[2];
        this.c = -10.0 * begin[0] - 6.0 * begin[1] - 1.5 * begin[2] + 10.0 * end[0] - 4.0 * end[1] + 0.5 * end[2];
        this.d = 0.5 * begin[2];
        this.e = begin[1];
        this.f = begin[0];
    }

    public double[] get(double t, int n) {
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            result[i] = switch (i) {
                case 0 -> ((((a * t + b) * t + c) * t + d) * t + e) * t + f;
                case 1 -> (((5.0 * a * t + 4.0 * b) * t + 3.0 * c) * t + 2.0 * d) * t + e;
                case 2 -> ((20.0 * a * t + 12.0 * b) * t + 6.0 * c) * t + 2.0 * d;
                case 3 -> (60.0 * a * t + 24.0 * b) * t + 6.0 * c;
                case 4 -> 120.0 * a * t + 24.0 * b;
                case 5 -> 120.0 * a;
                default -> 0;
            };
        }
        return result;
    }

    @Override
    public String toString() {
        return "QuinticSpline1D{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                ", e=" + e +
                ", f=" + f +
                '}';
    }
}
