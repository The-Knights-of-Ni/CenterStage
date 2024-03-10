package org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller;

import org.firstinspires.ftc.teamcode.Util.Pose;

public interface SwerveController extends ControllerOutput {
    SwerveControllerOutput calculate(Pose current, Pose target);

    /**
     * Resets the PID controller for the heading axis. This can reduce integral windup when applied properly.
     */
    void resetHeadingPID();
}
