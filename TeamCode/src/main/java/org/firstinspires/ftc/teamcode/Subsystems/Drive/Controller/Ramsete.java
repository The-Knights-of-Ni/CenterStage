package org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller;

import org.firstinspires.ftc.teamcode.Merlin.Curve.Arclength;
import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualNum;
import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualPose;
import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualPoseVelocity;
import org.firstinspires.ftc.teamcode.Merlin.Profile.Time;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.PoseVelocity;
import org.firstinspires.ftc.teamcode.Util.Vector;

import static org.firstinspires.ftc.teamcode.Merlin.MathUtils.snz;

public class Ramsete {
    double trackWidth;
    double zeta = 0.7;
    double bBar = 2.0;

    double b;

    public Ramsete(double trackWidth) {
        this.trackWidth = trackWidth;
        b = bBar / trackWidth;
    }

    private double sinc(double x) {
        double u = x + snz(x);
        return Math.sin(u) / u;
    }

    /**
     * Computes the velocity and acceleration command. The frame `Target` is the reference robot, and the frame `Actual`
     * is the measured, physical robot.
     *
     * @return velocity command in the actual frame
     */
    public DualPoseVelocity<Time> compute(
            DualNum<Time> s,
            DualPose<Arclength> targetPose,
            Pose actualPose
    ) {
        double vRef = s.get(1);
        double omegaRef = targetPose.reparam(s).heading.velocity().get(0);

        double k = 2.0 * zeta * Math.sqrt(omegaRef * omegaRef + b * vRef * vRef);


        Pose error = targetPose.value().minusExp(actualPose);
        return DualPoseVelocity.constant(
                new PoseVelocity(
                        new Vector(
                                vRef * error.heading.real + k * error.position.getX(),
                                0.0
                        ),
                        omegaRef + k * error.heading.log() + b * vRef * sinc(error.heading.log())
                ),
                2
        );
    }
}
