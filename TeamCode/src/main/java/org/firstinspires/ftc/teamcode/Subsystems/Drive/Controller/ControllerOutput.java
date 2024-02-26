package org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller;

/**
 * Merely a data class to hold the output of a {@link PositionController} or a {@link VAController},
 * this gets passed to a {@link org.firstinspires.ftc.teamcode.Subsystems.Drive.Localizer.Localizer}.
 */
public class ControllerOutput {
    public final double x;
    public final double y;
    public final double heading;
    public final double actualHeading;

    public ControllerOutput(double x, double y, double heading, double actualHeading) {
        this.x = x;
        this.y = y;
        this.heading = heading;
        this.actualHeading = actualHeading;
    }
}
