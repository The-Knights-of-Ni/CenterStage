package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller.HolonomicControllerOutput;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Localizer.AllWheelSwerveLocalizer;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotorGeneric;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Vector;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class SwerveDriveKinematicsTest {
    @Test
    void testStraight() {
        AllWheelSwerveLocalizer localizer = new AllWheelSwerveLocalizer(null);
        var actual = localizer.getModuleVelocities(new HolonomicControllerOutput(0.0, 1.0, 0.0, new Pose(0.0, 0.0, 0.0)));
        var expected = new MotorGeneric<>(
                new Vector(0.0, 1.0),
                new Vector(0.0, 1.0),
                new Vector(0.0, 1.0),
                new Vector(0.0, 1.0)
        );
        assert Objects.equals(actual.frontLeft, expected.frontLeft);
        assert Objects.equals(actual.frontRight, expected.frontRight);
        assert Objects.equals(actual.rearLeft, expected.rearLeft);
        assert Objects.equals(actual.rearRight, expected.rearRight);
    }

    @Test
    void testStrafe() {
        AllWheelSwerveLocalizer localizer = new AllWheelSwerveLocalizer(null);
        var actual = localizer.getModuleVelocities(new HolonomicControllerOutput(1.0, 0.0, 0.0, new Pose(0.0, 0.0, 0.0)));
        var expected = new MotorGeneric<>(
                new Vector(1.0, 0.0),
                new Vector(1.0, 0.0),
                new Vector(1.0, 0.0),
                new Vector(1.0, 0.0)
        );
        assert Objects.equals(actual.frontLeft, expected.frontLeft);
        assert Objects.equals(actual.frontRight, expected.frontRight);
        assert Objects.equals(actual.rearLeft, expected.rearLeft);
        assert Objects.equals(actual.rearRight, expected.rearRight);
    }
}
