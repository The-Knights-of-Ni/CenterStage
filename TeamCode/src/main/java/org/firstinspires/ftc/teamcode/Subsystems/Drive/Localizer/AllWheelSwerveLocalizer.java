package org.firstinspires.ftc.teamcode.Subsystems.Drive.Localizer;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller.HolonomicControllerOutput;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller.SwerveControllerOutput;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotorGeneric;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.SwerveGeneric;
import org.firstinspires.ftc.teamcode.Util.ServoEx;
import org.firstinspires.ftc.teamcode.Util.Vector;

public class AllWheelSwerveLocalizer extends SwerveLocalizer {
    public MotorGeneric<SwerveGeneric<DcMotorEx, ServoEx>> modules;
    public MotorGeneric<Double> angles;
    public final MotorGeneric<Vector3D> moduleDisplacements = new MotorGeneric<>(
            new Vector3D(-9, 9, 0),
            new Vector3D(9, 9, 0),
            new Vector3D(-9, -9, 0),
            new Vector3D(9, -9, 0)
    );

    public AllWheelSwerveLocalizer(MotorGeneric<SwerveGeneric<DcMotorEx, ServoEx>> modules) {
        this.modules = modules;
        this.angles = new MotorGeneric<>(0.0, 0.0, 0.0, 0.0);
    }

    public MotorGeneric<Vector3D> getModuleVelocities3D(HolonomicControllerOutput output) {
        var robotVelocity = new Vector3D(output.x, output.y, 0);
        var robotAngularVelocity = new Vector3D(0, 0, output.heading);
        return new MotorGeneric<>(
                robotVelocity.add(robotAngularVelocity.crossProduct(moduleDisplacements.frontLeft)),
                robotVelocity.add(robotAngularVelocity.crossProduct(moduleDisplacements.frontRight)),
                robotVelocity.add(robotAngularVelocity.crossProduct(moduleDisplacements.rearLeft)),
                robotVelocity.add(robotAngularVelocity.crossProduct(moduleDisplacements.rearRight))
        );
    }

    public MotorGeneric<Vector> getModuleVelocities(HolonomicControllerOutput output) {
        var velocities3D = getModuleVelocities3D(output);
        return new MotorGeneric<>(
                new Vector(velocities3D.frontLeft.getX(), velocities3D.frontLeft.getY()),
                new Vector(velocities3D.frontRight.getX(), velocities3D.frontRight.getY()),
                new Vector(velocities3D.rearLeft.getX(), velocities3D.rearLeft.getY()),
                new Vector(velocities3D.rearRight.getX(), velocities3D.rearRight.getY())
        );
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
