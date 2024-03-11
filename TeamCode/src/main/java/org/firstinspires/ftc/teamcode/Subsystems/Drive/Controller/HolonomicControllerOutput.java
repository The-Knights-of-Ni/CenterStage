package org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller;

import org.firstinspires.ftc.teamcode.Subsystems.Drive.Localizer.HolonomicLocalizer;
import org.firstinspires.ftc.teamcode.Util.Pose;

/**
 * Merely a data class to hold the output of a {@link HolonomicController},
 * this gets passed to a {@link HolonomicLocalizer}.
 */
public class HolonomicControllerOutput implements ControllerOutput {
    public final double x;
    public final double y;
    public final double heading;
    public final Pose currentPose;

    /**
     * Constructor for the SwerveControllerOutput class.
     *
     * @param x The x-coordinate for the controller output.
     * @param y The y-coordinate for the controller output.
     * @param heading The target rotation speed.
     * @param currentPose The actual heading direction (what the robot's current heading is).
     */
    public HolonomicControllerOutput(double x, double y, double heading, Pose currentPose) {
        this.x = x;
        this.y = y;
        this.heading = heading;
        this.currentPose = currentPose;
    }
}
