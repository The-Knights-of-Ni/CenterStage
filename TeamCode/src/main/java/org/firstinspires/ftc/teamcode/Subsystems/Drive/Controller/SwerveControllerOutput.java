package org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller;

import org.firstinspires.ftc.teamcode.Util.Pose;

public class SwerveControllerOutput implements ControllerOutput {
    public final double forward;
    public final double turn;
    public final Pose currentPose;


    public SwerveControllerOutput(double forward, double turn, Pose currentPose) {
        this.forward = forward;
        this.turn = turn;
        this.currentPose = currentPose;
    }
}
