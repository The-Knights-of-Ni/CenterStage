package org.firstinspires.ftc.teamcode.Subsystems.Drive.Localizer;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller.ControllerOutput;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotorGeneric;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.SwerveGeneric;
import org.firstinspires.ftc.teamcode.Util.ServoEx;
import org.firstinspires.ftc.teamcode.Util.Vector;

public class SwerveLocalizer extends Localizer {
    public MotorGeneric<SwerveGeneric<DcMotorEx, ServoEx>> modules;
    public MotorGeneric<Double> angles;

    public SwerveLocalizer(MotorGeneric<SwerveGeneric<DcMotorEx, ServoEx>> modules) {
        this.modules = modules;
        this.angles = new MotorGeneric<>(0.0, 0.0, 0.0, 0.0);
    }

    public MotorGeneric<SwerveGeneric<Double, Double>> localize(ControllerOutput output) {
        var movement = new Vector(output.x, output.y);
        return null;
    }

    @Override
    public void setPowers(ControllerOutput output) {
        var powers = localize(output);
        modules.frontLeft.motor.setPower(powers.frontLeft.motor);
        modules.frontRight.motor.setPower(powers.frontRight.motor);
        modules.rearLeft.motor.setPower(powers.rearLeft.motor);
        modules.rearRight.motor.setPower(powers.rearRight.motor);
        modules.frontLeft.servoOne.setPosition(powers.frontLeft.servoOne);
        modules.frontRight.servoOne.setPosition(powers.frontRight.servoOne);
        modules.rearLeft.servoOne.setPosition(powers.rearLeft.servoOne);
        modules.rearRight.servoOne.setPosition(powers.rearRight.servoOne);
        modules.frontLeft.servoTwo.setPosition(powers.frontLeft.servoTwo);
        modules.frontRight.servoTwo.setPosition(powers.frontRight.servoTwo);
        modules.rearLeft.servoTwo.setPosition(powers.rearLeft.servoTwo);
        modules.rearRight.servoTwo.setPosition(powers.rearRight.servoTwo);
    }
}
