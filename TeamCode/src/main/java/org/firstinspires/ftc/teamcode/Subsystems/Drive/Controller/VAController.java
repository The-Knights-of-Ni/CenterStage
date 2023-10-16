package org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller;

import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotionProfile.MotionProfileOutput;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotorGeneric;

public interface VAController {
    MotorGeneric<Double> calculate(MotionProfileOutput targetPose);
}
