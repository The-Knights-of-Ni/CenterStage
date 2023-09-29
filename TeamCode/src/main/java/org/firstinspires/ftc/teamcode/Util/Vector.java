package org.firstinspires.ftc.teamcode.Util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Vector extends Vector2D {
    /**
     * Simple constructor.
     * Build a vector from its coordinates
     *
     * @param x abscissa
     * @param y ordinate
     * @see #getX()
     * @see #getY()
     */
    public Vector(double x, double y) {
        super(x, y);
    }

    /**
     * Simple constructor.
     * Build a vector from its coordinates
     *
     * @param v coordinates array
     * @throws DimensionMismatchException if array does not have 2 elements
     * @see #toArray()
     */
    public Vector(double[] v) throws DimensionMismatchException {
        super(v);
    }

    /**
     * Multiplicative constructor
     * Build a vector from another one and a scale factor.
     * The vector built will be a * u
     *
     * @param a scale factor
     * @param u base (unscaled) vector
     */
    public Vector(double a, Vector2D u) {
        super(a, u);
    }

    /**
     * Linear constructor
     * Build a vector from two other ones and corresponding scale factors.
     * The vector built will be a1 * u1 + a2 * u2
     *
     * @param a1 first scale factor
     * @param u1 first base (unscaled) vector
     * @param a2 second scale factor
     * @param u2 second base (unscaled) vector
     */
    public Vector(double a1, Vector2D u1, double a2, Vector2D u2) {
        super(a1, u1, a2, u2);
    }

    /**
     * Linear constructor
     * Build a vector from three other ones and corresponding scale factors.
     * The vector built will be a1 * u1 + a2 * u2 + a3 * u3
     *
     * @param a1 first scale factor
     * @param u1 first base (unscaled) vector
     * @param a2 second scale factor
     * @param u2 second base (unscaled) vector
     * @param a3 third scale factor
     * @param u3 third base (unscaled) vector
     */
    public Vector(double a1, Vector2D u1, double a2, Vector2D u2, double a3, Vector2D u3) {
        super(a1, u1, a2, u2, a3, u3);
    }

    /**
     * Linear constructor
     * Build a vector from four other ones and corresponding scale factors.
     * The vector built will be a1 * u1 + a2 * u2 + a3 * u3 + a4 * u4
     *
     * @param a1 first scale factor
     * @param u1 first base (unscaled) vector
     * @param a2 second scale factor
     * @param u2 second base (unscaled) vector
     * @param a3 third scale factor
     * @param u3 third base (unscaled) vector
     * @param a4 fourth scale factor
     * @param u4 fourth base (unscaled) vector
     */
    public Vector(double a1, Vector2D u1, double a2, Vector2D u2, double a3, Vector2D u3, double a4, Vector2D u4) {
        super(a1, u1, a2, u2, a3, u3, a4, u4);
    }
}
