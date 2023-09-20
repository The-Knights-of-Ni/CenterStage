package org.firstinspires.ftc.teamcode.Util;

public class Pose {
    public final Vector position;
    public final Rotation heading;

    public Pose(Vector position, Rotation heading) {
        this.position = position;
        this.heading = heading;
    }

    public Pose(Vector position, double heading) {
        this(position, Rotation.exp(heading));
    }

    public Pose(double positionX, double positionY, double heading) {
        this(new Vector(positionX, positionY), heading);
    }

    public static Pose exp(Twist t) {
        Rotation heading = Rotation.exp(t.angle);

        double u = t.angle + snz(t.angle);
        double c = 1 - Math.cos(u);
        double s = Math.sin(u);
        double translationX = (s * t.line.getX() - c * t.line.getY()) / u;
        double translationY = (c * t.line.getX() + s * t.line.getY()) / u;

        return new Pose(new Vector(translationX, translationY), heading);
    }

    public Pose plus(Twist t) {
        return times(exp(t));
    }

    public Pose minusExp(Pose t) {
        return t.inverse().times(this);
    }

    public Twist minus(Pose t) {
        return minusExp(t).log();
    }

    public Pose times(Pose p) {
        return new Pose(new Vector(heading.times(p.position).add(position)), heading.times(p.heading));
    }

    public Vector times(Vector v) {
        return new Vector(heading.times(v).add(position));
    }

    public PoseVelocity times(PoseVelocity pv) {
        return new PoseVelocity(new Vector(heading.times(pv.linearVel).add(position)), pv.angVel);
    }

    public Pose inverse() {
        Rotation inverseHeading = heading.inverse();
        Vector inversePosition = inverseHeading.times(new Vector(-position.getX(), -position.getY()));
        return new Pose(inversePosition, inverseHeading);
    }

    public Twist log() {
        double theta = heading.log();

        double halfU = 0.5 * theta + snz(theta);
        double v = halfU / Math.tan(halfU);
        double translationX = v * position.getX() + halfU * position.getY();
        double translationY = -halfU * position.getX() + v * position.getY();

        return new Twist(new Vector(translationX, translationY), theta);
    }

    private static double snz(double theta) {
        return (theta < 1e-6 && theta > -1e-6) ? 1.0 : theta;
    }
}
