package org.firstinspires.ftc.teamcode.Subsystems.Drive.Localizer;

import org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller.HolonomicControllerOutput;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotorGeneric;

/**
 * A localizer is a class that takes in a controller output and sets the powers of the motors from that.
 */
public abstract class HolonomicLocalizer extends Localizer {
    /**
     * Sets the powers of the motors based on the controller output.
     * @param output The controller output to set the powers from.
     */
    public abstract void setPowers(HolonomicControllerOutput output);
}
