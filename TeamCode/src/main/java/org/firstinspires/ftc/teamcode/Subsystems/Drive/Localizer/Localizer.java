package org.firstinspires.ftc.teamcode.Subsystems.Drive.Localizer;

import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotorGeneric;

public abstract class Localizer {
    /**
     * Crops the motor powers to be within the range of -1 to 1. If they aren't it scales them down to be within that range.
     * @param powers The motor powers to crop.
     * @return The cropped motor powers.
     */
    public MotorGeneric<Double> cropMotorPowers(MotorGeneric<Double> powers) {
        var max = Math.max(Math.max(Math.abs(powers.frontLeft), Math.abs(powers.frontRight)),
                Math.max(Math.abs(powers.rearLeft), Math.abs(powers.rearRight)));
        if (max > 1) {
            return reduceDrivePowers(powers, 1 / max);
        }
        return powers;
    }

    /**
     * Scales the motor powers down by a factor.
     * @param powers The motor powers to scale down.
     * @param scalingFactor The factor to scale the motor powers down by.
     * @return The scaled down motor powers.
     */
    public MotorGeneric<Double> reduceDrivePowers(MotorGeneric<Double> powers, double scalingFactor) {
        return new MotorGeneric<>(powers.frontLeft * scalingFactor, powers.frontRight * scalingFactor,
                powers.rearLeft * scalingFactor, powers.rearRight * scalingFactor);
    }

}
