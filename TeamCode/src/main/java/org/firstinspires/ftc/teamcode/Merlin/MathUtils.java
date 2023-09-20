package org.firstinspires.ftc.teamcode.Merlin;

import java.util.ArrayList;
import java.util.List;

public class MathUtils {
    // ~10 * machine epsilon
    public static final double EPS = 2.2e-15;


    /**
     * Function \(snz(x)\) from section VI.A of the [SymForce paper](https://arxiv.org/abs/2204.07889) for use in
     * singularity handling.
     */
    public static double snz(double x) {
        return (x >= 0.0) ? EPS : -EPS;
    }

    public static double clamp(double x, double lo, double hi) {
        if (x < lo) {
            return lo;
        }
        if (x > hi) {
            return hi;
        }
        return x;
    }

    public static class MinMax {
        public final double min;
        public final double max;

        public MinMax(double min, double max) {
            this.min = min;
            this.max = max;
        }
    }

    /**
     * Partitions \([a, b]\) into \((n - 1)\) equal intervals and returns the endpoints.
     *
     * @param begin \(a\)
     * @param end   \(b\)
     * @param samples \(n\)
     */
    public static List<Double> range(double begin, double end, int samples) {
        if (samples < 2) {
            throw new IllegalArgumentException("Samples must be at least 2");
        }
        double dx = (end - begin) / (samples - 1);
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < samples; i++) {
            result.add(begin + dx * i);
        }
        return result;
    }

    /**
     * Partitions \([a, b]\) into \(n\) equal intervals and returns the center values.
     *
     * @param begin   \(a\)
     * @param end     \(b\)
     * @param samples \(n\)
     */
    public static List<Double> rangeCentered(double begin, double end, int samples) {
        if (samples < 1) {
            throw new IllegalArgumentException("Samples must be at least 1");
        }
        double dx = (end - begin) / samples;
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < samples; i++) {
            result.add(begin + 0.5 * dx + dx * i);
        }
        return result;
    }

    // TODO: is the branch here okay? would snz() on the denominator be better?
    public static double lerp(double x, double fromLo, double fromHi, double toLo, double toHi) {
        if (fromLo == fromHi) {
            return 0.0;
        } else {
            return toLo + (x - fromLo) * (toHi - toLo) / (fromHi - fromLo);
        }
    }

    public static class IntegralScanResult {
        public final List<Double> values;
        public final List<Double> sums;

        public IntegralScanResult(List<Double> values, List<Double> sums) {
            this.values = values;
            this.sums = sums;
        }
    }

    /**
     * Returns samples of \(g(t) = \int_a^t f(x) \, dx\) for various values \(a \leq t \leq b\).
     *
     * @param a   \(a\)
     * @param b   \(b\)
     * @param eps desired error in the length approximation \(g(b)\)
     * @param f   \(f(x)\)
     */
    public static IntegralScanResult integralScan(double a, double b, double eps, java.util.function.Function<Double, Double> f) {
        double m = (a + b) / 2;
        double fa = f.apply(a);
        double fm = f.apply(m);
        double fb = f.apply(b);

        double i = (b - a) / 8 * (
                fa + fm + fb +
                        f.apply(a + 0.9501 * (b - a)) +
                        f.apply(a + 0.2311 * (b - a)) +
                        f.apply(a + 0.6068 * (b - a)) +
                        f.apply(a + 0.4860 * (b - a)) +
                        f.apply(a + 0.8913 * (b - a))
        );
        if (i == 0.0) {
            i = b - a;
        }
        i *= eps / Math.ulp(1.0);

        List<Double> values = new ArrayList<>();
        List<Double> sums = new ArrayList<>();

        values.add(0.0);
        sums.add(0.0);

        List<List<Double>> resp =helper(i, a, m, b, fa, fm, fb, eps, f, values, sums);
        values = resp.get(0);
        sums = resp.get(1);
        return new IntegralScanResult(values, sums);
    }

    private static List<List<Double>> helper(double i, double a, double m, double b, double fa, double fm, double fb, double eps,
                                java.util.function.Function<Double, Double> f, List<Double> values, List<Double> sums) {
        double h = (b - a) / 4;
        double ml = a + h;
        double mr = b - h;
        double fml = f.apply(ml);
        double fmr = f.apply(mr);
        double i1 = h / 1.5 * (fa + 4 * fm + fb);
        double i2 = h / 3 * (fa + 4 * (fml + fmr) + 2 * fm + fb);
        i1 = (16 * i2 - i1) / 15;
        if (i + (i1 - i2) == i || m <= a || b <= m) {
            values.add(b);
            sums.add(sums.get(sums.size() - 1) + i1);
            ArrayList<List<Double>> resp = new ArrayList<>();
            resp.add(values);
            resp.add(sums);
            return resp;
        } else {
            List<List<Double>> res1 = helper(i, a, ml, m, fa, fml, fm, eps, f, values, sums);
            return helper(i, m, mr, b, fm, fmr, fb, eps, f, res1.get(0), res1.get(1));
        }
    }

    // precondition: source, target sorted and share the same length
    public static double lerpLookup(List<Double> source, List<Double> target, double query) {
        if (source.size() != target.size()) {
            throw new IllegalArgumentException("Source and target must have the same length");
        }
        if (source.isEmpty()) {
            throw new IllegalArgumentException("Source and target must not be empty");
        }

        int index = binarySearch(source, query);
        if (index >= 0) {
            return target.get(index);
        } else {
            int insIndex = -(index + 1);
            if (insIndex <= 0) {
                return target.get(0);
            } else if (insIndex >= source.size()) {
                return target.get(target.size() - 1);
            } else {
                double sLo = source.get(insIndex - 1);
                double sHi = source.get(insIndex);
                double tLo = target.get(insIndex - 1);
                double tHi = target.get(insIndex);
                return lerp(query, sLo, sHi, tLo, tHi);
            }
        }
    }

    // Helper method for binary search
    private static int binarySearch(List<Double> source, double query) {
        int left = 0;
        int right = source.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            double midValue = source.get(mid);
            if (midValue == query) {
                return mid;
            } else if (midValue < query) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -(left + 1); // Element not found, return insertion point
    }

    // precondition: source, target sorted and share the same length; queries sorted
    public static List<Double> lerpLookupMap(List<Double> source, List<Double> target, List<Double> queries) {
        if (source.size() != target.size()) {
            throw new IllegalArgumentException("Source and target must have the same length");
        }
        if (source.isEmpty()) {
            throw new IllegalArgumentException("Source and target must not be empty");
        }

        List<Double> result = new ArrayList<>();
        int i = 0;
        for (double query : queries) {
            if (query < source.get(0)) {
                result.add(target.get(0));
                continue;
            }

            while (i + 1 < source.size() && source.get(i + 1) < query) {
                i++;
            }

            if (i + 1 == source.size()) {
                result.add(target.get(target.size() - 1));
                continue;
            }

            double sLo = source.get(i);
            double sHi = source.get(i + 1);
            double tLo = target.get(i);
            double tHi = target.get(i + 1);
            result.add(lerp(query, sLo, sHi, tLo, tHi));
        }

        return result;
    }
}
