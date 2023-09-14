package org.firstinspires.ftc.teamcode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import android.util.Log;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Drive;
import org.firstinspires.ftc.teamcode.Util.Vector;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.mockingDetails;

class DriveTest {

    Drive init() {
        MockDcMotorEx mockFL = new MockDcMotorEx(DcMotor.RunMode.RUN_USING_ENCODER);
        MockDcMotorEx mockFR = new MockDcMotorEx(DcMotor.RunMode.RUN_USING_ENCODER);
        MockDcMotorEx mockRL = new MockDcMotorEx(DcMotor.RunMode.RUN_USING_ENCODER);
        MockDcMotorEx mockRR = new MockDcMotorEx(DcMotor.RunMode.RUN_USING_ENCODER);
        ElapsedTime timer = new ElapsedTime();
        MockTelemetry telemetry = new MockTelemetry();
        return new Drive(mockFL, mockFR, mockRL, mockRR, false, telemetry, timer);
    }

    @Test
    void testCalcMotorPower2D() {
        Drive drive = init();
        assertEquals(1, drive.calcMotorPowers(0, 1, 0)[0], 0.5);
        assertEquals(1, drive.calcMotorPowers(0, 1, 0)[1], 0.5);
        assertEquals(1, drive.calcMotorPowers(0, 1, 0)[2], 0.5);
        assertEquals(1, drive.calcMotorPowers(0, 1, 0)[3], 0.5);
        assertNotEquals(0, drive.calcMotorPowers(0, 1, 0)[0]);
        assertNotEquals(0, drive.calcMotorPowers(0, 1, 0)[1]);
        assertNotEquals(0, drive.calcMotorPowers(0, 1, 0)[2]);
        assertNotEquals(0, drive.calcMotorPowers(0, 1, 0)[3]);
    }

    @Test
    void testPIDBasic() {
        MockedStatic<Log> mocked = mockStatic(Log.class);
        Drive drive = init();
        drive.moveVector(new Vector(0, 1000));
        assertEquals(drive.frontLeft.getCurrentPosition(), 1782, 500);
        assertEquals(drive.frontRight.getCurrentPosition(), 1782, 500);
        assertEquals(drive.rearLeft.getCurrentPosition(), 1782, 500);
        assertEquals(drive.rearRight.getCurrentPosition(), 1782, 500);
    }
}
