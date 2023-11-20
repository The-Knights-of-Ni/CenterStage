package org.firstinspires.ftc.teamcode.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;

public class IntegralScan {
    public static class IntegralScanResult {
        List<Double> values;
        List<Double> sums;

        public IntegralScanResult(List<Double> values, List<Double> sums) {
            this.values = values;
            this.sums = sums;
        }
    }

    List<Double> values;
    List<Double> sums;
    double a;
    double b;
    double eps;
    double i;
    double m;
    double fa;
    double fm;
    double fb;
    double i;
    ToDoubleFunction<Double> f;

    public IntegralScan(double a, double b, double eps, ToDoubleFunction<Double> f) {
        this.a = a;
        this.b = b;
        this.eps = eps;
        this.f = f;
        this.values = new ArrayList<>(1);
        values.add(0.0);
        this.sums = new ArrayList<>(1);
        sums.add(0.0);
        m = (a + b) / 2;
        fa = f.applyAsDouble(a);
        fm = f.applyAsDouble(m);
        fb = f.applyAsDouble(b);
        i = (b - a) / 8 * (
                fa + fm + fb +
                        f.applyAsDouble(a + 0.9501 * (b - a)) +
                        f.applyAsDouble(a + 0.2311 * (b - a)) +
                        f.applyAsDouble(a + 0.6068 * (b - a)) +
                        f.applyAsDouble(a + 0.4860 * (b - a)) +
                        f.applyAsDouble(a + 0.8913 * (b - a))
        );
        if (i == 0.0) {
            i = b - a;
        }
        i *= eps / Math.ulp(1.0);

    }

    void helper(double a, double m, double b, double fa, double fm, double fb) {
        var h = (b - a) / 4;
        var ml = a + h;
        var mr = b - h;
        var fml = f.applyAsDouble(ml);
        var fmr = f.applyAsDouble(mr);
        var i1 = h / 1.5 * (fa + 4 * fm + fb);
        var i2 = h / 3 * (fa + 4 * (fml + fmr) + 2 * fm + fb);
        i1 = (16 * i2 - i1) / 15;
        if (i + (i1 - i2) == i || m <= a || b <= m) {
            values.add(b);
            sums.add(sums.get(sums.size() - 1) + i1);
        } else {
            helper(a, ml, m, fa, fml, fm);
            helper(m, mr, b, fm, fmr, fb);
        }
    }

    /**
     * Returns samples of \(g(t) = \int_a^t f(x) \, dx\) for various values \(a \leq t \leq b\). The sampling points are
     * chosen adaptively using the algorithm `adaptsim` from [Gander and Gautschi](https://doi.org/10.1023/A:1022318402393)
     * ([more accessible link](https://users.wpi.edu/~walker/MA510/HANDOUTS/w.gander,w.gautschi,Adaptive_Quadrature,BIT_40,2000,84-101.pdf)).
     *
     * @param[a] \(a\)
     * @param[b] \(b\)
     * @param[f] \(f(x)\)
     * @param[eps] desired error in the length approximation \(g(b)\)
     */
    public IntegralScanResult scan() {
        helper(a, m, b, fa, fm, fb);
        return new IntegralScanResult(values, sums);
    }
}
