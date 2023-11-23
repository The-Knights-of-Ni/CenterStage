package org.firstinspires.ftc.teamcode.Subsystems.Drive.Localizer;

import org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller.ControllerOutput;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotorGeneric;

public abstract class Localizer {
    public MotorGeneric<Double> cropMotorPowers(MotorGeneric<Double> powers) {
        var max = Math.max(Math.max(Math.abs(powers.frontLeft), Math.abs(powers.frontRight)),
                Math.max(Math.abs(powers.rearLeft), Math.abs(powers.rearRight)));
        if (max > 1) {
            return reduceDrivePowers(powers, 1 / max);
        }
        return powers;
    }

    public MotorGeneric<Double> reduceDrivePowers(MotorGeneric<Double> powers, double scalingFactor) {
        return new MotorGeneric<>(powers.frontLeft * scalingFactor, powers.frontRight * scalingFactor,
                powers.rearLeft * scalingFactor, powers.rearRight * scalingFactor);
    }

    public abstract MotorGeneric<Double> localize(ControllerOutput output);

    public MotorGeneric<Double> mix(ControllerOutput first, ControllerOutput second, int firstMultiplier, int secondMultiplier) {
        var firstMotorPowers = localize(first);
        var secondMotorPowers = localize(second);
        return cropMotorPowers(new MotorGeneric<>(firstMotorPowers.frontLeft * firstMultiplier + secondMotorPowers.frontLeft * secondMultiplier,
                firstMotorPowers.frontRight * firstMultiplier + secondMotorPowers.frontRight * secondMultiplier,
                firstMotorPowers.rearLeft * firstMultiplier + secondMotorPowers.rearLeft * secondMultiplier,
                firstMotorPowers.rearRight * firstMultiplier + secondMotorPowers.rearRight * secondMultiplier
        ));
    }

    public MotorGeneric<Double> mix(ControllerOutput first, ControllerOutput second) {
        return mix(first, second, 1, 1);
    }
}
