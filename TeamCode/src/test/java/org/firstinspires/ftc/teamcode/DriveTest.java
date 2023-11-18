package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Subsystems.Drive.Drive.thetaPIDCoefficients;
import static org.firstinspires.ftc.teamcode.Subsystems.Drive.Drive.xyPIDCoefficients;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import android.util.Log;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller.ControllerOutput;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller.HolonomicPositionController;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Drive;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Localizer.Localizer;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Localizer.MecanumLocalizer;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotorGeneric;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.PID;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.PoseEstimationMethodChoice;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Vector;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.Mockito.mockStatic;

class DriveTest {
    // The margins will get smaller over time, as the mocking improves and the PID becomes more calibrated.
    final static int PID_TICK_COUNT_MARGIN = 500;

    Drive init() {
        MockDcMotorEx mockFL = new MockDcMotorEx(DcMotor.RunMode.RUN_USING_ENCODER);
        MockDcMotorEx mockFR = new MockDcMotorEx(DcMotor.RunMode.RUN_USING_ENCODER);
        MockDcMotorEx mockRL = new MockDcMotorEx(DcMotor.RunMode.RUN_USING_ENCODER);
        MockDcMotorEx mockRR = new MockDcMotorEx(DcMotor.RunMode.RUN_USING_ENCODER);
        MockTelemetry telemetry = new MockTelemetry();
        BNO055IMU mockIMU = Mockito.mock(BNO055IMU.class);
        return new Drive(new MotorGeneric<>(mockFL, mockFR, mockRL, mockRR), null, PoseEstimationMethodChoice.MOTOR_ENCODERS, mockIMU, telemetry);
    }

    @Test
    void testCalcMotorPower2D() {
        Drive drive = init();
        assertEquals(1, drive.calcMotorPowers(0, 1, 0).frontLeft, 0.4);
        assertEquals(1, drive.calcMotorPowers(0, 1, 0).frontRight, 0.4);
        assertEquals(1, drive.calcMotorPowers(0, 1, 0).rearLeft, 0.4);
        assertEquals(1, drive.calcMotorPowers(0, 1, 0).rearRight, 0.4);
        assertNotEquals(0, drive.calcMotorPowers(0, 1, 0).frontLeft);
        assertNotEquals(0, drive.calcMotorPowers(0, 1, 0).frontRight);
        assertNotEquals(0, drive.calcMotorPowers(0, 1, 0).rearLeft);
        assertNotEquals(0, drive.calcMotorPowers(0, 1, 0).rearRight);
    }

    @Test
    void testPIDBasic() {
        try (MockedStatic<Log> mocked = mockStatic(Log.class)) {
            Drive drive = init();
            drive.moveVector(new Vector(0, 1000));
            assertEquals(1782, drive.motors.frontLeft.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
            assertEquals(1782, drive.motors.frontRight.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
            assertEquals(1782, drive.motors.rearLeft.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
            assertEquals(1782, drive.motors.rearRight.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
        }
    }

    @Test
    void testPIDStrafe() {
        try (MockedStatic<Log> mocked = mockStatic(Log.class)) {
            Drive drive = init();
            drive.moveVector(new Vector(1000, 0));
            assertEquals(2442, drive.motors.frontLeft.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
            assertEquals(-2442, drive.motors.frontRight.getCurrentPosition(), PID_TICK_COUNT_MARGIN * 2);
            assertEquals(-2442, drive.motors.rearLeft.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
            assertEquals(2442, drive.motors.rearRight.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
        }
    }


    @Test
    void testPIDDiagonal() {
        try (MockedStatic<Log> mocked = mockStatic(Log.class)) {
            Drive drive = init();
            drive.moveVector(new Vector(1000, 1000));
            assertEquals(4224, drive.motors.frontLeft.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
            assertEquals(-659, drive.motors.frontRight.getCurrentPosition(), PID_TICK_COUNT_MARGIN * 2);
            assertEquals(-659, drive.motors.rearLeft.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
            assertEquals(4224, drive.motors.rearRight.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
        }
    }

    @Test
    void testPIDTurn() {
        try (MockedStatic<Log> mocked = mockStatic(Log.class)) {
            Drive drive = init();
            drive.move(new Pose(0, 0, 90));
            assertEquals(-1170, drive.motors.frontLeft.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
            assertEquals(1170, drive.motors.frontRight.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
            assertEquals(-1170, drive.motors.rearLeft.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
            assertEquals(1170, drive.motors.rearRight.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
        }
    }

    @Test
    void testHolonomicMecanumController() {
        HolonomicPositionController controller = new HolonomicPositionController(new PID(xyPIDCoefficients), new PID(xyPIDCoefficients), new PID(thetaPIDCoefficients));
        Localizer localizer = new MecanumLocalizer();
        MotorGeneric<Double> powers = localizer.localize(controller.calculate(new Pose(0, 0, 0), new Pose(0, 0, 0)));
        assertEquals(0, powers.frontLeft, 0.01);
        assertEquals(0, powers.frontRight, 0.01);
        assertEquals(0, powers.rearLeft, 0.01);
        assertEquals(0, powers.rearRight, 0.01);
    }

    @Test
    void testHolonomicController() {
        HolonomicPositionController controller = new HolonomicPositionController(new PID(xyPIDCoefficients), new PID(xyPIDCoefficients), new PID(thetaPIDCoefficients));
        var powers = controller.calculate(new Pose(0, 0, 0), new Pose(1000, 0, 0));
        assertEquals(1, powers.x, 0.3);
    }

    @Test
    void testMecanumLocalizer() {
        Localizer localizer = new MecanumLocalizer();
        var resp = localizer.localize(new ControllerOutput(0, 1, 0, 0));
        assertEquals(1, resp.frontLeft);
        assertEquals(1, resp.frontRight);
        assertEquals(1, resp.rearLeft);
        assertEquals(1, resp.rearRight);
    }
}
