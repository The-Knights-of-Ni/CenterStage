package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import org.firstinspires.ftc.teamcode.Util.Pose;

public class HolonomicController implements Controller {
    PID xControl;
    PID yControl;
    PID thetaControl;

    public HolonomicController(PID x, PID y, PID theta) {
        // Three PID controllers for actual robot movement instead of motor movement

        this.xControl = x;
        this.yControl = y;
        this.thetaControl = theta;
    }


    public MotorGeneric<Double> cropMotorPowers(MotorGeneric<Double> powers) {
        double max = Math.max(Math.max(Math.abs(powers.frontLeft), Math.abs(powers.frontRight)),
                Math.max(Math.abs(powers.rearLeft), Math.abs(powers.rearRight)));
        if (max > 1) {
            return new MotorGeneric<>(powers.frontLeft / max, powers.frontRight / max,
                    powers.rearLeft / max, powers.rearRight / max);
        }
        return powers;
    }

    public MotorGeneric<Double> reduceDrivePowers(MotorGeneric<Double> powers, double scalingFactor) {
        return new MotorGeneric<>(powers.frontLeft * scalingFactor, powers.frontRight * scalingFactor,
                powers.rearLeft * scalingFactor, powers.rearRight * scalingFactor);
    }

    @Override
    public MotorGeneric<Double> calculate(Pose current, Pose target) {
        double xPower = xControl.calculate(target.x * Drive.COUNTS_PER_MM, current.x * Drive.COUNTS_PER_MM);
        double yPower = yControl.calculate(target.y * Drive.COUNTS_PER_MM, current.y * Drive.COUNTS_PER_MM);
        double thetaPower = thetaControl.calculate(target.heading * Drive.COUNTS_PER_MM, current.heading * Drive.COUNTS_PER_MM);
        double yRotated = xPower * Math.cos(target.heading) - yPower * Math.sin(target.heading); // Inverted bc api
        double xRotated = xPower * Math.sin(target.heading) + yPower * Math.cos(target.heading);
        return reduceDrivePowers(cropMotorPowers(
                        new MotorGeneric<>(
                                xRotated + yRotated + thetaPower,
                                xRotated - yRotated + thetaPower,
                                xRotated - yRotated - thetaPower,
                                xRotated + yRotated - thetaPower)
                ),
                0.5);
    }
}
