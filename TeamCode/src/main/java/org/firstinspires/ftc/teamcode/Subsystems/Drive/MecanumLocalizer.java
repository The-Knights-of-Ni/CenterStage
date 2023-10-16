package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller.ControllerOutput;
import org.firstinspires.ftc.teamcode.Subsystems.Localizer;

public class MecanumLocalizer extends Localizer {
    public MecanumLocalizer() {

    }

    public MotorGeneric<Double> localize(ControllerOutput output) {
        var xPower = output.x();
        var yPower = output.y();
        var thetaPower = output.heading();
        var yRotated = xPower * Math.cos(output.actualHeading()) - yPower * Math.sin(output.actualHeading()); // Inverted bc api
        var xRotated = xPower * Math.sin(output.actualHeading()) + yPower * Math.cos(output.actualHeading());
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