package org.firstinspires.ftc.teamcode.Merlin.Builder;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualPose;
import org.firstinspires.ftc.teamcode.Merlin.Curve.Arclength;
import org.firstinspires.ftc.teamcode.Util.Pose;

public abstract class PoseMap {
    public abstract DualPose<Arclength> map(DualPose<Arclength> pose);

    public Pose map(Pose pose) {
        return map(DualPose.constant(pose, 1)).value();
    }
}
