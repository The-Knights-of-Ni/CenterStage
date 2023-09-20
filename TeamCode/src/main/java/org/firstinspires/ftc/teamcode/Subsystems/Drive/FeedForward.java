package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Merlin.Curve.Arclength;
import org.firstinspires.ftc.teamcode.Merlin.PositionPath;

public class FeedForward {
    private double Kv;
    private double Ka;

    protected ElapsedTime timer = new ElapsedTime();

    public FeedForward(double Kv, double Ka) {
        this.Kv = Kv;
        this.Ka = Ka;
    }

    /**
     * calculate output (hacky time/dynamic programming being used) TODO: standardize
     *
     * @return output
     */
    public double calculate(double velocityReference, double accelerationReference) {
        // Cap output at range (-1,1)
        return Math.min(1, Math.max(-1, Kv * velocityReference + Ka * accelerationReference));
    }
}