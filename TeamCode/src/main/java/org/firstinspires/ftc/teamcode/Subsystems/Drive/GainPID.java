package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import androidx.core.math.MathUtils;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Util.LookupTable;

/**
 * <p>PID controller with gain scheduling. Uses interpolated lookup tables internally.</p>
 * <p><b>Features:</b></p>
 * <ul>
 *     <li>Toggleable Low Pass Filter</li>
 *     <li>Integral Windup Prevention</li>
 *     <li>Integral Power Cap</li>
 * </ul>
 */
public class GainPID {
    protected boolean hasRun = false;
    protected ElapsedTime timer = new ElapsedTime();
    protected double previousError = 0;
    protected double integralSum = 0;
    protected double derivative = 0;
    protected double previousDerivative = 0;
    public static final double derivativeInverseFilterStrength = 0.7; // TODO: Should be configurable
    private final LookupTable Kp;
    private final LookupTable Ki;
    private final LookupTable Kd;
    private final boolean lowPass;

    public GainPID(LookupTable Kp, LookupTable Ki, LookupTable Kd, boolean lowPass) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
        this.lowPass = lowPass;
    }

    public GainPID(LookupTable Kp, LookupTable Ki, LookupTable Kd) {
        this(Kp, Ki, Kd, true);
    }

    public GainPID(PIDCoefficients<LookupTable> coefficients) {
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
//        if (Math.signum(error) != Math.signum(previousError)) {
//            integralSum = 0; // Prevents integral windup
//        }
        integrate(error, dt);
        previousError = error;

        // Note that integral sum*ki is capped at 0.25 to not break everything.
        var iTerm = integralSum * Ki.interpolate(measured);
        // TODO: If sign of kp term is not the same as the integral term, then don't use it and log warning.
        // Cap output at range (-1,1).
        return MathUtils.clamp(error * Kp.interpolate(measured) + iTerm + derivative * Kd.interpolate(measured), -1, 1);
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
        if (lowPass) {
            derivative = GainPID.derivativeInverseFilterStrength * previousDerivative + derivative * (1 - GainPID.derivativeInverseFilterStrength);
        }
        return derivative;
    }

    @Override
    public String toString() {
        return "PID {" +
                "Kp=" + Kp +
                ", Ki=" + Ki +
                ", Kd=" + Kd +
                ", lowPass=" + lowPass +
                ", dump=" + previousError + "|" + derivative + "|" + integralSum +
                '}';
    }

    public void reset() {
        integralSum = 0;
    }
}