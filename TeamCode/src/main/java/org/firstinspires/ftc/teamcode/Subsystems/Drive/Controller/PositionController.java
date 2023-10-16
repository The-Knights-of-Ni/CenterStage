package org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller;

import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotorGeneric;
import org.firstinspires.ftc.teamcode.Util.Pose;

public interface PositionController {
    MotorGeneric<Double> calculate(Pose current, Pose target);

    void resetHeadingPID();
}
