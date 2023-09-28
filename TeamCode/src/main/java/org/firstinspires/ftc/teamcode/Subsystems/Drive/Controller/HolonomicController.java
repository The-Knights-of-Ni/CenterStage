package org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualPose;
import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualPoseVelocity;
import org.firstinspires.ftc.teamcode.Merlin.Profile.Time;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.PoseVelocity;
import org.firstinspires.ftc.teamcode.Util.Vector;

public class HolonomicController{

    double axialPosGain;
    double lateralPosGain;
    double headingGain;
    double axialVelGain;
    double lateralVelGain;
    double headingVelGain;

    public HolonomicController(double axialPosGain, double lateralPosGain, double headingGain, double axialVelGain, double lateralVelGain, double headingVelGain) {
        this.axialPosGain = axialPosGain;
        this.lateralPosGain = lateralPosGain;
        this.headingGain = headingGain;
        this.axialVelGain = axialVelGain;
        this.lateralVelGain = lateralVelGain;
        this.headingVelGain = headingVelGain;
    }

    public HolonomicController(
            double axialPosGain,
            double lateralPosGain,
            double headingGain
    ) {
        this(axialPosGain, lateralPosGain, headingGain, 0.0, 0.0, 0.0);
    }

    /**
     * Computes the velocity and acceleration command. The frame `Target` is the reference robot, and the frame `Actual`
     * is the measured, physical robot.
     *
     * @return velocity command in the actual frame
     */
    public DualPoseVelocity<Time> compute(
            DualPose<Time> targetPose,
            Pose actualPose,
            PoseVelocity actualVelActual
    ) {
        DualPoseVelocity<Time> targetVelWorld = targetPose.velocity();
        DualPose<Time> txActualWorld = DualPose.<Time>constant(actualPose.inverse(), 2);
        DualPoseVelocity<Time> targetVelActual = txActualWorld.times(targetVelWorld);

        PoseVelocity velErrorActual = targetVelActual.value().minus(actualVelActual);

        Pose error = targetPose.value().minusExp(actualPose);
        return targetVelActual.plus(
                new PoseVelocity(
                        new Vector(
                                axialPosGain * error.position.getX(),
                                lateralPosGain * error.position.getY()
                        ),
                        headingGain * error.heading.log()
                )
        ).plus(new PoseVelocity(
                new Vector(
                        axialVelGain * velErrorActual.linearVel.getX(),
                        lateralVelGain * velErrorActual.linearVel.getY()
                ),
                headingVelGain * velErrorActual.angVel
        ));
    }
}
