package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import com.qualcomm.robotcore.util.ElapsedTime;

public class FeedForward implements MoveSystem {
    private double Kv;
    private double Ka;

    protected boolean hasRun = false;

    protected ElapsedTime timer = new ElapsedTime();

    private double previousMeasured = 0;
    private double previousTime = 0;

    public FeedForward(double Kv, double Ka) {
        this.Kv = Kv;
        this.Ka = Ka;
        timer.reset();
    }

    /**
     * calculate output (hacky time/dynamic programming being used) TODO: standardize
     * @param target the target position
     * @param measured current system state
     * @return output
     */
    public double calculate(double target, double measured) {
        double currentVelocity = (measured - previousMeasured) / (timer.seconds() - previousTime);
        double velocityReference = 0.7; // TODO: Don't hardcode
        double accelerationReference = velocityReference - currentVelocity + 0.1; // TODO: Calibrate
        // Cap output at range (-1,1)
        double cappedOutput = Math.min(1, Math.max(-1, Kv * velocityReference + Ka * accelerationReference));
        previousTime = timer.seconds();
        previousMeasured = measured;
        return cappedOutput;
    }
}