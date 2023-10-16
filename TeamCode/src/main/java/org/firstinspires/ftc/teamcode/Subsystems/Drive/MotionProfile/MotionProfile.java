package org.firstinspires.ftc.teamcode.Subsystems.Drive.MotionProfile;

public interface MotionProfile {
    MotionProfileOutput2D calculate(double time);

    boolean isFinished(double time);
}
