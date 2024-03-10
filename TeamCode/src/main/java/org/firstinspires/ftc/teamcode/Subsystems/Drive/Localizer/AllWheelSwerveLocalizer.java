package org.firstinspires.ftc.teamcode.Subsystems.Drive.Localizer;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller.HolonomicControllerOutput;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller.SwerveControllerOutput;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotorGeneric;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.SwerveGeneric;
import org.firstinspires.ftc.teamcode.Util.ServoEx;

public class AllWheelSwerveLocalizer extends SwerveLocalizer {
    public MotorGeneric<SwerveGeneric<DcMotorEx, ServoEx>> modules;
    public MotorGeneric<Double> angles;

    public AllWheelSwerveLocalizer(MotorGeneric<SwerveGeneric<DcMotorEx, ServoEx>> modules) {
        this.modules = modules;
        this.angles = new MotorGeneric<>(0.0, 0.0, 0.0, 0.0);
    }

    public MotorGeneric<Double> getAngles(SwerveControllerOutput output) {
        return null;
    }

    public MotorGeneric<Double> getPowers(HolonomicControllerOutput output) {
        return null;
    }

    public MotorGeneric<SwerveGeneric<Double, Double>> localize(SwerveControllerOutput output) {

        return null;
    }

    @Override
    public void setPowers(SwerveControllerOutput output) {
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
