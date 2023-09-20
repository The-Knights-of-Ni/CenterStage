package org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation;


import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Twist;

/**
 * Dual version of Pose.
 */
public class DualPose<Param> {
    public final DualVector<Param> position;
    public final DualRotation<Param> heading;

    public DualPose(DualVector<Param> position, DualRotation<Param> heading) {
        this.position = position;
        this.heading = heading;
    }

    public static <Param> DualPose<Param> constant(Pose p, int n) {
        return new DualPose<>(DualVector.constant(p.position, n), DualRotation.constant(p.heading, n));
    }

    public DualPose<Param> plus(Twist t) {
        return times(Pose.exp(t));
    }

    public DualPose<Param> times(Pose p) {
        return new DualPose<>(heading.times(p.position).plus(position), heading.times(p.heading));
    }

    public DualPose<Param> times(DualPose<Param> p) {
        return new DualPose<>(heading.times(p.position).plus(position), heading.times(p.heading));
    }

    public DualPoseVelocity<Param> times(DualPoseVelocity<Param> pv) {
        return new DualPoseVelocity<>(heading.times(pv.linearVel).plus(position), pv.angVel);
    }

    public DualPoseVelocity<Param> velocity() {
        return new DualPoseVelocity<>(position.drop(1), heading.velocity());
    }

    public DualPose<Param> inverse() {
        DualRotation<Param> inverseHeading = heading.inverse();
        DualVector<Param> inversePosition = inverseHeading.times(new DualVector<>(position.getX().unaryMinus(), position.getY().unaryMinus()));
        return new DualPose<>(inversePosition, inverseHeading);
    }

    public <NewParam> DualPose<NewParam> reparam(DualNum<NewParam> oldParam) {
        return new DualPose<>(position.reparam(oldParam), heading.reparam(oldParam));
    }

    public Pose value() {
        return new Pose(position.value(), heading.value());
    }
}
