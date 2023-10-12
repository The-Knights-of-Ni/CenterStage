package org.firstinspires.ftc.teamcode.Subsystems.Drive.MotionProfile;

import org.firstinspires.ftc.teamcode.Util.Pose;

public interface MotionProfile {
    Pose calculate(double time);
}
