package org.firstinspires.ftc.teamcode.Util;

public class Rotation {
    public final double real;
    public final double imag;

    public Rotation(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public static Rotation exp(double theta) {
        return new Rotation(Math.cos(theta), Math.sin(theta));
    }

    public Rotation plus(double x) {
        return times(exp(x));
    }

    public Rotation minus(Rotation r) {
        return r.inverse().times(this).log();
    }

    public Vector times(Vector v) {
        return new Vector(real * v.x - imag * v.y, imag * v.x + real * v.y);
    }

    public PoseVelocity times(PoseVelocity pv) {
        return new PoseVelocity(times(pv.linearVel), pv.angVel);
    }

    public Rotation times(Rotation r) {
        return new Rotation(real * r.real - imag * r.imag, real * r.imag + imag * r.real);
    }

    public Vector vec() {
        return new Vector(real, imag);
    }

    public Rotation inverse() {
        return new Rotation(real, -imag);
    }

    public double log() {
        return Math.atan2(imag, real);
    }
}