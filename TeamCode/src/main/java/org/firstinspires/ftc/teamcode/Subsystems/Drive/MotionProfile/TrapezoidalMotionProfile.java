package org.firstinspires.ftc.teamcode.Subsystems.Drive.MotionProfile;

import org.firstinspires.ftc.teamcode.Util.Pose;

public class TrapezoidalMotionProfile implements MotionProfile {
    public double maxAcceleration;
    public double maxVelocity;
    public Pose target;
    public TrapezoidalMotionProfile1D x;
    public TrapezoidalMotionProfile1D y;
    public TrapezoidalMotionProfile1D heading;

    public TrapezoidalMotionProfile(double max_acceleration, double max_velocity, Pose target) {
        this.maxAcceleration = max_acceleration;
        this.maxVelocity = max_velocity;
        this.target = target;
        this.x = new TrapezoidalMotionProfile1D(max_acceleration, max_velocity, target.x);
        this.y = new TrapezoidalMotionProfile1D(max_acceleration, max_velocity, target.y);
        this.heading = new TrapezoidalMotionProfile1D(0, 0, 0); // TODO: Implement heading with non-trapezoidal
    }

    @Override
    public MotionProfileOutput calculate(double time) {
        return new MotionProfileOutput(x.calculate(time), y.calculate(time), heading.calculate(time));
    }

    @Override
    public boolean isFinished(double time) {
        return x.isFinished(time) && y.isFinished(time) && heading.isFinished(time);
    }
}
