package org.firstinspires.ftc.teamcode.Merlin.Trajectory;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualPose;
import org.firstinspires.ftc.teamcode.Merlin.Curve.PosePath;
import org.firstinspires.ftc.teamcode.Merlin.Profile.DisplacementProfile;
import org.firstinspires.ftc.teamcode.Merlin.Profile.Time;
import org.firstinspires.ftc.teamcode.Util.Vector;

public class DisplacementTrajectory{
    PosePath path;
    DisplacementProfile profile;

    public DisplacementTrajectory(PosePath path, DisplacementProfile profile) {
        this.path = path;
        this.profile = profile;
    }

    public DisplacementTrajectory(Trajectory t) {
        this(t.path, t.profile.baseProfile);
    }

    public double length() {
        return path.length();
    }

    public void project(Vector query, double init) {
        project(path, query, init);
    }

    public DualPose<Time> get(double s) {
        return path.get(s,3).reparam(profile.get(s));
    }
}
