package org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation;


import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Rotation;
import org.firstinspires.ftc.teamcode.Util.Vector;

public class DualRotation<Param> {
    public final DualNum<Param> real;
    public final DualNum<Param> imag;

    public DualRotation(DualNum<Param> real, DualNum<Param> imag) {
        this.real = real;
        this.imag = imag;
        assert (real.size() == imag.size());
        assert (real.size() <= 3);
    }

    public static <Param> DualRotation<Param> exp(DualNum<Param> theta) {
        return new DualRotation<>(theta.cos(), theta.sin());
    }

    public static <Param> DualRotation<Param> constant(Rotation r, int n) {
        return new DualRotation<>(DualNum.constant(r.real, n), DualNum.constant(r.imag, n));
    }

    public int size() {
        return real.size();
    }

    public DualRotation<Param> plus(double x) {
        return times(Rotation.exp(x));
    }

    public DualRotation<Param> plus(DualNum<Param> d) {
        return times(exp(d));
    }

    public DualPoseVelocity<Param> times(DualPoseVelocity<Param> pv) {
        return new DualPoseVelocity<>(times(pv.linearVel), pv.angVel);
    }

    public DualVector<Param> times(DualVector<Param> v) {
        return new DualVector<>(
            real.times(v.getX()).minus(imag.times(v.getY())),
            imag.times(v.getX()).plus(real.times(v.getY()))
        );
    }

    public DualVector<Param> times(Vector v) {
        return new DualVector<>(
            real.times(v.getX()).minus(imag.times(v.getY())),
            imag.times(v.getX()).plus(real.times(v.getY()))
        );
    }

    public DualRotation<Param> times(DualRotation<Param> r) {
        return new DualRotation<>(
            real.times(r.real).minus(imag.times(r.imag)),
            real.times(r.imag).plus(imag.times(r.real))
        );
    }

    public DualRotation<Param> times(Rotation r) {
        return new DualRotation<>(
            real.times(r.real).minus(imag.times(r.imag)),
            real.times(r.imag).plus(imag.times(r.real))
        );
    }

    public DualPose<Param> times(Pose p) {
        return new DualPose<>(times(p.position), times(p.heading));
    }

    public DualRotation<Param> inverse() {
        return new DualRotation<>(real, imag.unaryMinus());
    }

    public <NewParam> DualRotation<NewParam> reparam(DualNum<NewParam> oldParam) {
        return new DualRotation<>(real.reparam(oldParam), imag.reparam(oldParam));
    }

    public DualNum<Param> velocity() {
        return real.times(imag.drop(1)).minus(imag.times(real.drop(1)));
    }

    public Rotation value() {
        return new Rotation(real.value(), imag.value());
    }
}

