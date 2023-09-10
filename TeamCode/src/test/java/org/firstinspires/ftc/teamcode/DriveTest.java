package org.firstinspires.ftc.teamcode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Drive;
import org.firstinspires.ftc.teamcode.Util.Vector;
import org.junit.jupiter.api.Test;

class DriveTest {

    Drive init() {
        MockDcMotorEx mockFL = new MockDcMotorEx(DcMotor.RunMode.RUN_USING_ENCODER);
        MockDcMotorEx mockFR = new MockDcMotorEx(DcMotor.RunMode.RUN_USING_ENCODER);
        MockDcMotorEx mockRL = new MockDcMotorEx(DcMotor.RunMode.RUN_USING_ENCODER);
        MockDcMotorEx mockRR = new MockDcMotorEx(DcMotor.RunMode.RUN_USING_ENCODER);
        ElapsedTime timer = new ElapsedTime();
        MockTelemetry telemetry = new MockTelemetry();
        return new Drive(mockFL, mockFR, mockRL, mockRR, telemetry, timer);
    }

    @Test
    void testCalcMotorPower2D() {
        Drive drive = init();
        assertEquals(0.5, drive.calcMotorPowers(0, 1, 0)[0], 0.5);
        assertEquals(0.5, drive.calcMotorPowers(0, 1, 0)[1], 0.5);
        assertEquals(0.5, drive.calcMotorPowers(0, 1, 0)[2], 0.5);
        assertEquals(0.5, drive.calcMotorPowers(0, 1, 0)[3], 0.5);
        assertNotEquals(0, drive.calcMotorPowers(0, 1, 0)[0]);
        assertNotEquals(0, drive.calcMotorPowers(0, 1, 0)[1]);
        assertNotEquals(0, drive.calcMotorPowers(0, 1, 0)[2]);
        assertNotEquals(0, drive.calcMotorPowers(0, 1, 0)[3]);
    }
}
