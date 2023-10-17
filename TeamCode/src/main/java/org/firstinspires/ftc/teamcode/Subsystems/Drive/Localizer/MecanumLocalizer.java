package org.firstinspires.ftc.teamcode.Subsystems.Drive.Localizer;

import org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller.ControllerOutput;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotorGeneric;

public class MecanumLocalizer extends Localizer {
    public MecanumLocalizer() {

    }

    public MotorGeneric<Double> localize(ControllerOutput output) {
        var xPower = output.x;
        var yPower = output.y;
        var thetaPower = output.heading;
        var yRotated = xPower * Math.cos(output.actualHeading) - yPower * Math.sin(output.actualHeading); // Inverted bc api
        var xRotated = xPower * Math.sin(output.actualHeading) + yPower * Math.cos(output.actualHeading);
        return cropMotorPowers(
                new MotorGeneric<>(
                        xRotated + yRotated + thetaPower,
                        xRotated - yRotated + thetaPower,
                        xRotated - yRotated - thetaPower,
                        xRotated + yRotated - thetaPower)
        );
    }
}
