package org.firstinspires.ftc.teamcode.Subsystems.Drive.MotionProfile;

import org.firstinspires.ftc.teamcode.Util.Pose;

public class TrapezoidalMotionProfile implements MotionProfile {
    public double maxAcceleration;
    public double maxVelocity;
    public Pose target;
    public TrapezoidalMotionProfile1D x;
    public TrapezoidalMotionProfile1D y;

    public TrapezoidalMotionProfile(double max_acceleration, double max_velocity, Pose target) {
        this.maxAcceleration = max_acceleration;
        this.maxVelocity = max_velocity;
        this.target = target;
        this.x = new TrapezoidalMotionProfile1D(max_acceleration, max_velocity, target.x);
        this.y = new TrapezoidalMotionProfile1D(max_acceleration, max_velocity, target.y);
    }

    @Override
    public Pose calculate(double time) {
        return new Pose(x.calculate(time), y.calculate(time), target.heading);
    }
}
