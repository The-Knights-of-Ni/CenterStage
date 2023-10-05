package org.firstinspires.ftc.teamcode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import android.util.Log;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Drive;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotorGeneric;
import org.firstinspires.ftc.teamcode.Util.Vector;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.mockingDetails;

class DriveTest {
    // The margins will get smaller over time, as the mocking improves and the PID becomes more calibrated.
    final static int PID_TICK_COUNT_MARGIN = 500;

    Drive init() {
        MockDcMotorEx mockFL = new MockDcMotorEx(DcMotor.RunMode.RUN_USING_ENCODER);
        MockDcMotorEx mockFR = new MockDcMotorEx(DcMotor.RunMode.RUN_USING_ENCODER);
        MockDcMotorEx mockRL = new MockDcMotorEx(DcMotor.RunMode.RUN_USING_ENCODER);
        MockDcMotorEx mockRR = new MockDcMotorEx(DcMotor.RunMode.RUN_USING_ENCODER);
        ElapsedTime timer = new ElapsedTime();
        MockTelemetry telemetry = new MockTelemetry();
        BNO055IMU mockIMU = Mockito.mock(BNO055IMU.class);
        return new Drive(new MotorGeneric<>(mockFL, mockFR, mockRL, mockRR), null, mockIMU, telemetry, timer);
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
            // TODO: Once fr gets fixed we can uncomment this test :)
            // assertEquals(1782, drive.frontRight.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
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
            // TODO: Once fr gets fixed we can uncomment this test :)
            // assertEquals(-2442, drive.frontRight.getCurrentPosition(), PID_TICK_COUNT_MARGIN * 2);
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
            // TODO: Once fr gets fixed we can uncomment this test :)
            // assertEquals(-659, drive.frontRight.getCurrentPosition(), PID_TICK_COUNT_MARGIN * 2);
            assertEquals(-659, drive.motors.rearLeft.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
            assertEquals(4224, drive.motors.rearRight.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
        }
    }

    @Test
    void testPIDTurn() {
        try (MockedStatic<Log> mocked = mockStatic(Log.class)) {
            Drive drive = init();
            drive.moveVector(new Vector(0, 0), 90);
            assertEquals(-1170, drive.motors.frontLeft.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
            // TODO: Once fr gets fixed we can uncomment this test :)
            // assertEquals(1170, drive.frontRight.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
            assertEquals(-1170, drive.motors.rearLeft.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
            assertEquals(1170, drive.motors.rearRight.getCurrentPosition(), PID_TICK_COUNT_MARGIN);
        }
    }
}
