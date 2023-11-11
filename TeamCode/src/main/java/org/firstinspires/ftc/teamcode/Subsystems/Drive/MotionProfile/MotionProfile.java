package org.firstinspires.ftc.teamcode.Subsystems.Drive.MotionProfile;

public interface MotionProfile {
    MotionProfileOutput calculate(double time);

    boolean isFinished(double time);
}
