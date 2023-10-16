package org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller;

import org.firstinspires.ftc.teamcode.Subsystems.Drive.FeedForward;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotionProfile.MotionProfileOutput;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotorGeneric;

public class HolonomicVAController implements VAController {
    FeedForward xControl;
    FeedForward yControl;
    FeedForward thetaControl;

    public HolonomicVAController(FeedForward x, FeedForward y, FeedForward theta) {
        // Three PID controllers for actual robot movement instead of motor movement

        this.xControl = x;
        this.yControl = y;
        this.thetaControl = theta;
    }


    public MotorGeneric<Double> cropMotorPowers(MotorGeneric<Double> powers) {
        var max = Math.max(Math.max(Math.abs(powers.frontLeft), Math.abs(powers.frontRight)),
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
    public MotorGeneric<Double> calculate(MotionProfileOutput target) {
        var xPower = xControl.calculate(target.x().velocity(), target.x().velocity());
        var yPower = yControl.calculate(target.y().velocity(), target.y().velocity());
        var thetaPower = thetaControl.calculate(target.heading().velocity(), target.heading().velocity());
        var yRotated = xPower * Math.cos(target.heading().position()) - yPower * Math.sin(target.heading().position()); // Inverted bc api
        var xRotated = xPower * Math.sin(target.heading().position()) + yPower * Math.cos(target.heading().position());
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
