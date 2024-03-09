package org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller;

import org.firstinspires.ftc.teamcode.Util.Pose;

/**
 * A position controller is one that uses a PID controller to control the robot's position.
 */
public interface PositionController {
    ControllerOutput calculate(Pose current, Pose target);

    void resetHeadingPID();
}
