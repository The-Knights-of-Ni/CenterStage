package org.firstinspires.ftc.teamcode.Subsystems.Drive;

public class FeedForward {
    private double kV;
    private double kA;

    public FeedForward(double kV, double kA) {
        this.kV = kV;
        this.kA = kA;
    }

    public double calculate(double velocity, double acceleration) {
        return kV * velocity + kA * acceleration;
    }
}
