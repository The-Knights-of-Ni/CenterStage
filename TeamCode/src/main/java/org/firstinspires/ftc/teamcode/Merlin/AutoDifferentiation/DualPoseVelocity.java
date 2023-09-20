package org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation;

import org.firstinspires.ftc.teamcode.Util.PoseVelocity;

/**
 * Dual version of PoseVelocity2d.
 */
public class DualPoseVelocity<Param> {
    public final DualVector<Param> linearVel;
    public final DualNum<Param> angVel;

    public DualPoseVelocity(DualVector<Param> linearVel, DualNum<Param> angVel) {
        this.linearVel = linearVel;
        this.angVel = angVel;
    }

    public static <Param> DualPoseVelocity<Param> constant(PoseVelocity pv, int n) {
        return new DualPoseVelocity<>(DualVector.constant(pv.linearVel, n), DualNum.constant(pv.angVel, n));
    }

    public DualPoseVelocity<Param> plus(PoseVelocity other) {
        return new DualPoseVelocity<>(linearVel.plus(other.linearVel), angVel.plus(other.angVel));
    }

    public PoseVelocity value() {
        return new PoseVelocity(linearVel.value(), angVel.value());
    }
}
