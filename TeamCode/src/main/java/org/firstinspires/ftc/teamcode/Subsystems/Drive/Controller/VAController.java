package org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller;

import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotionProfile.MotionProfileOutput;

public interface VAController {
    ControllerOutput calculate(double heading, MotionProfileOutput targetPose);
}
