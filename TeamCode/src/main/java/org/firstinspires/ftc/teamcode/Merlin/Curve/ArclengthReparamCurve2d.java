package org.firstinspires.ftc.teamcode.Merlin.Curve;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualNum;
import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualVector;
import org.firstinspires.ftc.teamcode.Merlin.MathUtils;
import org.firstinspires.ftc.teamcode.Merlin.PositionPath;

import static org.firstinspires.ftc.teamcode.Merlin.MathUtils.integralScan;
import static org.firstinspires.ftc.teamcode.Merlin.MathUtils.lerpLookup;

public class ArclengthReparamCurve2d extends PositionPath<Arclength> {
    PositionPath<Internal> curve;
    MathUtils.IntegralScanResult samples;
    double length;

    /**
     * @param[eps] desired error in the approximate length [length]
     */
    public ArclengthReparamCurve2d(
            PositionPath<Internal> curve,
            double eps
    ) {
        this.curve = curve;
        samples = integralScan(0.0, curve.length(), eps, aDouble -> curve.get(aDouble, 2).drop(1).value().getNorm());
    }

    public double reparam(double s) {
        return lerpLookup(samples.sums, samples.values, s);
    }

    @Override
    public DualVector<Arclength> get(double param, int n) {
        double t = reparam(param);
        DualVector<Internal> point = curve.get(t, n);

        double[] tValues = new double[n];
        tValues[0] = t;
        if (n <= 1)
            return point.reparam(new DualNum<>(tValues));

        DualNum<Internal> tDerivs = point.drop(1).norm().recip();
        tValues[1] = tDerivs.get(0);
        if (n <= 2) {
            return point.reparam(new DualNum<>(tValues));
        }
        tValues[2] = tDerivs.reparam(new DualNum<>(tValues)).get(1);
        if (n <= 3) {
            return point.reparam(new DualNum<>(tValues));
        }
        tValues[3] = tDerivs.reparam(new DualNum<Arclength>(tValues)).get(2);
        return point.reparam(new DualNum<>(tValues));
    }

    @Override
    public double length() {
        return length;
    }
}
