package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import androidx.core.math.MathUtils;
import com.qualcomm.robotcore.util.ElapsedTime;

public class PID {
    protected boolean hasRun = false;
    protected ElapsedTime timer = new ElapsedTime();
    protected double previousError = 0;
    protected double integralSum = 0;
    protected double derivative = 0;
    protected double previousDerivative = 0;
    private double derivativeInverseFilterStrength = 0.7;
    private final double Kp;
    private final double Ki;
    private final double Kd;

    public PID(double Kp, double Ki, double Kd) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
    }

    public PID(PIDCoefficients coefficients) {
        this(coefficients.kP, coefficients.kI, coefficients.kD);
    }

    /**
     * calculate PID output
     *
     * @param target   the target position
     * @param measured current system state
     * @return PID output
     */
    public double calculate(double target, double measured) {
        double dt = getDT();
        double error = calculateError(target, measured);
        double derivative = calculateDerivative(error, dt);
        integrate(error, dt);
        previousError = error;

        // Cap output at range (-1,1)
        return MathUtils.clamp(error * Kp + Math.max(integralSum * Ki, 0.25) + derivative * Kd, -1, 1);
    }

    /**
     * get the time constant
     *
     * @return time constant
     */
    public double getDT() {
        if (!hasRun) {
            hasRun = true;
            timer.reset();
        }
        double dt = timer.seconds();
        timer.reset();
        return dt;
    }

    protected double calculateError(double target, double measured) {
        return target - measured;
    }

    protected void integrate(double error, double dt) {
        integralSum += ((error + previousError) / 2) * dt;
    }

    protected double calculateDerivative(double error, double dt) {
        previousDerivative = derivative;
        derivative = (error - previousError) / dt;
        derivative = derivativeInverseFilterStrength * previousDerivative + derivative * (1 - derivativeInverseFilterStrength);
        return derivative;
    }

    @Override
    public String toString() {
        return "PID{" +
                "Kp=" + Kp +
                ", Ki=" + Ki +
                ", Kd=" + Kd +
                '}';
    }

    public void reset() {
        integralSum = 0; // TODO: test
    }
}