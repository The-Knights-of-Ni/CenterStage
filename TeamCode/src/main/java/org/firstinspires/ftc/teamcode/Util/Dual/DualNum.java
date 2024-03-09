package org.firstinspires.ftc.teamcode.Util.Dual;

import java.util.List;

/**
 *
 * @param <Param> Variable of differentiation
 */
public class DualNum<Param> {
    double[] values;

    /**
     * Initialize the autodifferentiation with a list of values, the public alternative is {@link #DualNum(List)}
     * @param values
     */
    private DualNum(double[] values) {
        this.values = values;
    }

    /**
     * Store the values in a list and call the private constructor
     * @param values
     */
    public DualNum(List<Double> values) {
        this(values.stream().mapToDouble(Double::doubleValue).toArray());
    }

    /**
     * Make a DualNum that is a constant. The derivative of a constant is 0, so it's just a list of the constant and 0s.
     */
    public <Param> DualNum<Param> constant(double c, int n) {
        double[] array = new double[n];
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                array[i] = c;
            } else {
                array[i] = 0.0;
            }
        }
        return new DualNum<>(array);
    }

    public DualNum<Param> variable(double x0, int n) {
        double[] array = new double[n];
        for (int i = 0; i < n; i++) {
            switch (i) {
                case 0:
                    array[i] = x0;
                    break;
                case 1:
                    array[i] = 1.0;
                    break;
                default:
                    array[i] = 0.0;
                    break;
            }
        }
        return new DualNum<>(array);
    }

    public int size() {
        return values.length;
    }
}
