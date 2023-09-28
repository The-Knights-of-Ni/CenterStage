package org.firstinspires.ftc.teamcode.Merlin.Builder;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualPose;
import org.firstinspires.ftc.teamcode.Merlin.Curve.Arclength;
import org.firstinspires.ftc.teamcode.Merlin.Curve.PosePath;

public class MappedPosePath extends PosePath {
    PosePath basePath;
    PoseMap poseMap;

    @Override
    public double length() {
        return basePath.length();
    }

    @Override
    public DualPose<Arclength> get(double s, int n) {
        return poseMap.map(basePath.get(s, n));
    }
}
