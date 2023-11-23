package org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller;

import org.firstinspires.ftc.teamcode.Util.Pose;

public interface PositionController {
    ControllerOutput calculate(Pose current, Pose target);

    void resetHeadingPID();
}
