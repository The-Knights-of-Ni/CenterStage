package org.firstinspires.ftc.teamcode.Merlin.Trajectory;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualPose;
import org.firstinspires.ftc.teamcode.Merlin.Builder.MappedPosePath;
import org.firstinspires.ftc.teamcode.Merlin.Curve.Arclength;
import org.firstinspires.ftc.teamcode.Merlin.Curve.PosePath;
import org.firstinspires.ftc.teamcode.Merlin.Profile.CancelableProfile;

import java.util.List;

public class Trajectory {
    MappedPosePath path;
    CancelableProfile profile;
    List<Double> offsets;

    public Trajectory(MappedPosePath path, CancelableProfile profile, List<Double> offsets) {
        this.path = path;
        this.profile = profile;
        this.offsets = offsets;
    }

    public DisplacementTrajectory cancel(double s) {
        double offset = s;
        return new DisplacementTrajectory(new PosePath() {
            public double length() {
                return path.length() - offset;
            }

            public DualPose<Arclength> get(double s, int n) {
                return path.get(s + offset);
            }
        },
                profile.cancel(s)
        );
    }
}
