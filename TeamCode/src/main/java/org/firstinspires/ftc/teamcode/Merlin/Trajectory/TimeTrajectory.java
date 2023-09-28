package org.firstinspires.ftc.teamcode.Merlin.Trajectory;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualNum;
import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualPose;
import org.firstinspires.ftc.teamcode.Merlin.Curve.PosePath;
import org.firstinspires.ftc.teamcode.Merlin.Profile.Time;
import org.firstinspires.ftc.teamcode.Merlin.Profile.TimeProfile;

public class TimeTrajectory {
    public PosePath path;
    TimeProfile profile;
    double duration;

    public TimeTrajectory(PosePath path, TimeProfile profile) {
        this.path = path;
        this.profile = profile;
        this.duration = profile.duration;
    }

    public TimeTrajectory(Trajectory t) {
        this(t.path, new TimeProfile(t.profile.baseProfile));
    }

    public TimeTrajectory(DisplacementTrajectory t) {
        this(t.path, new TimeProfile(t.profile));
    }

    public DualPose<Time> get(double t) {
        DualNum<Time> s = profile.get(t);
        return path.get(s.value(), 3).reparam(s);
    }
}
