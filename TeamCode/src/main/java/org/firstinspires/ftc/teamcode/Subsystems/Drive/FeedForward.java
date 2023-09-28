package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.apache.commons.math3.util.MathUtils;
import org.firstinspires.ftc.teamcode.Merlin.Curve.Arclength;
import org.firstinspires.ftc.teamcode.Merlin.PositionPath;

public class FeedForward {
    private double Ks;
    private double Kv;
    private double Ka;

    public FeedForward(double Ks, double Kv, double Ka) {
        this.Ks = Ks;
        this.Kv = Kv;
        this.Ka = Ka;
    }

    /**
     * calculate output (hacky time/dynamic programming being used) TODO: standardize
     *
     * @return output
     */
    public double calculate(double velocityReference, double accelerationReference) {
        // Cap output at range (-1,1) TODO: Use Ks
        return Math.min(1, Math.max(-1, Kv * velocityReference + Ka * accelerationReference));
    }
}