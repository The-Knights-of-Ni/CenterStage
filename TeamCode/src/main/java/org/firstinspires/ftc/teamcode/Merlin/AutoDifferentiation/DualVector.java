package org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation;

import org.firstinspires.ftc.teamcode.Util.Vector;

public class DualVector<Param> {
    private final DualNum<Param> x;
    private final DualNum<Param> y;

    public DualVector(DualNum<Param> x, DualNum<Param> y) {
        this.x = x;
        this.y = y;
    }

    public static <Param> DualVector<Param> constant(Vector v, int n) {
        return new DualVector<>(DualNum.constant(v.getX(), n), DualNum.constant(v.getY(), n));
    }

    public DualNum<Param> getX() {
        return x;
    }

    public DualNum<Param> getY() {
        return y;
    }

    public DualVector<Param> plus(Vector v) {
        return new DualVector<>(x.plus(v.getX()), y.plus(v.getY()));
    }

    public DualVector<Param> plus(DualVector<Param> v) {
        return new DualVector<>(x.plus(v.x), y.plus(v.y));
    }

    public DualVector<Param> minus(DualVector<Param> v) {
        return new DualVector<>(x.minus(v.x), y.minus(v.y));
    }

    public DualVector<Param> unaryMinus() {
        return new DualVector<>(x.unaryMinus(), y.unaryMinus());
    }

    public DualVector<Param> div(double z) {
        return new DualVector<>(x.div(z), y.div(z));
    }

    public DualNum<Param> dot(DualVector<Param> v) {
        return x.times(v.x).plus(y.times(v.y));
    }

    public DualNum<Param> sqrNorm() {
        return this.dot(this);
    }

    public DualNum<Param> norm() {
        return sqrNorm().sqrt();
    }

    public DualVector<Param> bind() {
        return new DualVector<>(x, y);
    }

    public <NewParam> DualVector<NewParam> reparam(DualNum<NewParam> oldParam) {
        return new DualVector<>(x.reparam(oldParam), y.reparam(oldParam));
    }

    public DualVector<Param> drop(int n) {
        return new DualVector<>(x.drop(n), y.drop(n));
    }

    public Vector value() {
        return new Vector(x.value(), y.value());
    }

//    public Rotation2dDual<Param> angleCast() {
//        return new Rotation2dDual<>(x, y);
//    }
}
