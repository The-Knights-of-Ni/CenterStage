package org.firstinspires.ftc.teamcode.Subsystems.Drive.MotionProfile;

public class MotionProfileOutput {
    public final MotionProfileOutput1D x;
    public final MotionProfileOutput1D y;
    public final MotionProfileOutput1D heading;

    public MotionProfileOutput(MotionProfileOutput1D x, MotionProfileOutput1D y, MotionProfileOutput1D heading) {
        this.x = x;
        this.y = y;
        this.heading = heading;
    }
}
