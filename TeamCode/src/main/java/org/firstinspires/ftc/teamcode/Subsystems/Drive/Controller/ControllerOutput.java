package org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller;

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
