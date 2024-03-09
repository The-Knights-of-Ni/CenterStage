package org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller;

import org.firstinspires.ftc.teamcode.Util.Pose;

/**
 * Merely a data class to hold the output of a {@link PositionController} or a {@link VAController},
 * this gets passed to a {@link org.firstinspires.ftc.teamcode.Subsystems.Drive.Localizer.Localizer}.
 */
public class ControllerOutput {
    public final double x;
    public final double y;
    public final double heading;
    public final Pose currentPose;

    /**
     * Constructor for the ControllerOutput class.
     *
     * @param x The x-coordinate for the controller output.
     * @param y The y-coordinate for the controller output.
     * @param heading The target rotation speed.
     * @param currentPose The actual heading direction (what the robot's current heading is).
     */
    public ControllerOutput(double x, double y, double heading, Pose currentPose) {
        this.x = x;
        this.y = y;
        this.heading = heading;
        this.currentPose = currentPose;
    }
}
