package org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation;

import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * [Dual number](https://en.wikipedia.org/wiki/Dual_number) to implement forward autodifferentiation.
 *
 * @param <Param> \(x\)
 * @property values \(\left(u, \frac{du}{dx}, \frac{d^2u}{dx^2}, \ldots, \frac{d^{n - 1} u}{dx^{n - 1}} \right)\)
 */
public class DualNum<Param> {
    private final double[] values;

    public DualNum(double[] values) {
        this.values = values;
    }

    public DualNum(List<Double> values) {
        this.values = new double[values.size()];
        for (int i = 0; i < values.size(); i++) {
            this.values[i] = values.get(i);
        }
    }

    public static <Param> DualNum<Param> constant(double c, int n) {
        double[] values = new double[n];
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                values[i] = c;
            } else {
                values[i] = 0.0;
            }
        }
        return new DualNum<>(values);
    }

    public static <Param> DualNum<Param> variable(double x0, int n) {
        double[] values = new double[n];
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                values[i] = x0;
            } else if (i == 1) {
                values[i] = 1.0;
            } else {
                values[i] = 0.0;
            }
        }
        return new DualNum<>(values);
    }

    public static <Param> DualNum<Param> cons(double x, DualNum<Param> d) {
        double[] values = new double[d.size() + 1];
        for (int i = 0; i <= d.size(); i++) {
            if (i == 0) {
                values[i] = x;
            } else {
                values[i] = d.values[i - 1];
            }
        }
        return new DualNum<>(values);
    }

    public int size() {
        return values.length;
    }

    public double value() {
        return values[0];
    }

    public double get(int i) {
        return values[i];
    }

    public List<Double> values() {
        List<Double> resp = new ArrayList<>();
        for (double value: values) {
            resp.add(value);
        }
        return resp;
    }

    public DualNum<Param> drop(int n) {
        double[] newValues = new double[size() - n];
        for (int i = 0; i < newValues.length; i++) {
            newValues[i] = values[i + n];
        }
        return new DualNum<>(newValues);
    }

    public DualNum<Param> plus(DualNum<Param> d) {
        int minLength = Math.min(size(), d.size());
        double[] newValues = new double[minLength];
        for (int i = 0; i < minLength; i++) {
            newValues[i] = values[i] + d.values[i];
        }
        return new DualNum<>(newValues);
    }

    public DualNum<Param> minus(DualNum<Param> d) {
        int minLength = Math.min(size(), d.size());
        double[] newValues = new double[minLength];
        for (int i = 0; i < minLength; i++) {
            newValues[i] = values[i] - d.values[i];
        }
        return new DualNum<>(newValues);
    }

    public DualNum<Param> times(DualNum<Param> d) {
        int minLength = Math.min(size(), d.size());
        double[] newValues = new double[minLength];
        if (newValues.length == 0) {
            return new DualNum<>(newValues);
        }

        newValues[0] = values[0] * d.values[0];
        if (newValues.length == 1) {
            return new DualNum<>(newValues);
        }

        newValues[1] = values[0] * d.values[1] + values[1] * d.values[0];
        if (newValues.length == 2) {
            return new DualNum<>(newValues);
        }

        newValues[2] = values[0] * d.values[2] + values[2] * d.values[0] + 2 * values[1] * d.values[1];
        if (newValues.length == 3) {
            return new DualNum<>(newValues);
        }

        newValues[3] = values[0] * d.values[3] + values[3] * d.values[0] + 3 * (values[2] * d.values[1] + values[1] * d.values[2]);
        return new DualNum<>(newValues);
    }

    public DualNum<Param> unaryMinus() {
        double[] newValues = new double[size()];
        for (int i = 0; i < size(); i++) {
            newValues[i] = -values[i];
        }
        return new DualNum<>(newValues);
    }

    public DualNum<Param> recip() {
        double[] newValues = new double[size()];
        if (newValues.length == 0) {
            return new DualNum<>(newValues);
        }

        double recip = 1.0 / values[0];
        newValues[0] = recip;
        if (newValues.length == 1) {
            return new DualNum<>(newValues);
        }

        double negRecip = -recip;
        double negRecip2 = recip * negRecip;
        double deriv = negRecip2 * values[1];
        newValues[1] = deriv;
        if (newValues.length == 2) {
            return new DualNum<>(newValues);
        }

        double int1 = 2 * negRecip * deriv;
        double deriv2 = int1 * values[1] + negRecip2 * values[2];
        newValues[2] = deriv2;
        if (newValues.length == 3) {
            return new DualNum<>(newValues);
        }

        double int2 = int1 * values[2];
        newValues[3] = int2 + negRecip2 * values[3] + int2 - 2 * (deriv * deriv + recip * deriv2) * values[1];
        return new DualNum<>(newValues);
    }

    public DualNum<Param> div(DualNum<Param> d) {
        return times(d.recip());
    }

    public DualNum<Param> sqrt() {
        double[] newValues = new double[size()];
        if (newValues.length == 0) {
            return new DualNum<>(newValues);
        }

        double sqrt = Math.sqrt(values[0]);
        newValues[0] = sqrt;
        if (newValues.length == 1) {
            return new DualNum<>(newValues);
        }

        double recip = 1 / (2 * sqrt);
        double deriv = recip * values[1];
        newValues[1] = deriv;
        if (newValues.length == 2) {
            return new DualNum<>(newValues);
        }

        double negRecip = -2 * recip;
        double negRecip2 = recip * negRecip;
        double int1 = negRecip2 * deriv;
        double secondDeriv = int1 * values[1] + recip * values[2];
        newValues[2] = secondDeriv;
        if (newValues.length == 3) {
            return new DualNum<>(newValues);
        }

        double int2 = 2 * int1;
        newValues[3] = recip * values[3] + int2 * values[2] + (deriv * negRecip * int2 + negRecip2 * secondDeriv) * values[1];

        return new DualNum<>(newValues);
    }

    public DualNum<Param> sin() {
        double[] newValues = new double[size()];
        if (newValues.length == 0) {
            return new DualNum<>(newValues);
        }

        double sin = Math.sin(values[0]);
        newValues[0] = sin;
        if (newValues.length == 1) {
            return new DualNum<>(newValues);
        }

        double cos = Math.cos(values[0]);
        double deriv = cos * values[1];
        newValues[1] = deriv;
        if (newValues.length == 2) {
            return new DualNum<>(newValues);
        }

        double inDeriv2 = values[1] * values[1];
        newValues[2] = cos * values[2] - sin * inDeriv2;
        if (newValues.length == 3) {
            return new DualNum<>(newValues);
        }

        newValues[3] = cos * values[3] - 3 * sin * values[1] * values[2] - deriv * inDeriv2;

        return new DualNum<>(newValues);
    }

    public DualNum<Param> cos() {
        double[] newValues = new double[size()];
        if (newValues.length == 0) {
            return new DualNum<>(newValues);
        }

        double cos = Math.cos(values[0]);
        newValues[0] = cos;
        if (newValues.length == 1) {
            return new DualNum<>(newValues);
        }

        double sin = Math.sin(values[0]);
        double negInDeriv = -values[1];
        double deriv = sin * negInDeriv;
        newValues[1] = deriv;
        if (newValues.length == 2) {
            return new DualNum<>(newValues);
        }

        double intVal = cos * negInDeriv;
        newValues[2] = intVal * values[1] - sin * values[2];
        if (newValues.length == 3) {
            return new DualNum<>(newValues);
        }

        newValues[3] = deriv * negInDeriv * values[1] + 3 * intVal * values[2] - sin * values[3];

        return new DualNum<>(newValues);
    }

    /**
     * Reparameterizes \(\left(x, \frac{dx}{du}, \frac{d^2x}{du^2}, \ldots, \frac{d^{n - 1} x}{du^{n - 1}}\right)\) into
     * \(\left(x, \frac{dx}{dt}, \frac{d^2x}{dt^2}, \ldots, \frac{d^{n - 1} x}{dt^{n - 1}}\right)\) using [oldParam] and
     * the chain rule.
     *
     * @param <NewParam> \(\left(u, \frac{du}{dt}, \frac{d^2u}{dt^2}, \ldots, \frac{d^{n - 1} u}{dt^{n - 1}}\right)\)
     */
    public <NewParam> DualNum<NewParam> reparam(DualNum<NewParam> oldParam) {
        int minLength = Math.min(size(), oldParam.size());
        double[] newValues = new double[minLength];
        if (newValues.length == 0) {
            return new DualNum<>(newValues);
        }

        newValues[0] = values[0];
        if (newValues.length == 1) {
            return new DualNum<>(newValues);
        }

        newValues[1] = values[1] * oldParam.get(1);
        if (newValues.length == 2) {
            return new DualNum<>(newValues);
        }

        double oldDeriv2 = oldParam.get(1) * oldParam.get(1);
        newValues[2] = oldDeriv2 * values[2] + oldParam.get(2) * values[1];
        if (newValues.length == 3) {
            return new DualNum<>(newValues);
        }

        newValues[3] = values[1] * oldParam.get(3) +
                (3 * values[2] * oldParam.get(2) + values[3] * oldDeriv2) * oldParam.get(1);

        return new DualNum<>(newValues);
    }

    public DualNum<Param> plus(double c) {
        double[] newValues = new double[size()];
        for (int i = 0; i < size(); i++) {
            if (i == 0) {
                newValues[i] = values[0] + c;
            } else {
                newValues[i] = values[i];
            }
        }
        return new DualNum<>(newValues);
    }

    public DualNum<Param> minus(double c) {
        double[] newValues = new double[size()];
        for (int i = 0; i < size(); i++) {
            if (i == 0) {
                newValues[i] = values[0] - c;
            } else {
                newValues[i] = values[i];
            }
        }
        return new DualNum<>(newValues);
    }

    public DualNum<Param> times(double c) {
        double[] newValues = new double[size()];
        for (int i = 0; i < size(); i++) {
            newValues[i] = values[i] * c;
        }
        return new DualNum<>(newValues);
    }

    public DualVector<Param> times(Vector c) {
        return new DualVector<>(this.times(c.getX()), this.times(c.getY()));
    }

    public DualNum<Param> div(double c) {
        double[] newValues = new double[size()];
        for (int i = 0; i < size(); i++) {
            newValues[i] = values[i] / c;
        }
        return new DualNum<>(newValues);
    }
}

