package org.theknightsofni.pidrs;

import org.firstinspires.ftc.teamcode.Subsystems.Drive.Drive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PIDTest {
    @Test
    void testInit() {
        PID.nativeRun();
    }

    @Test
    void testCalcMotorPower2D() {
        assertEquals(1, PID.nativeCalcMotorPowers(0, 1, 0)[0], 0.4);
        assertEquals(1, PID.nativeCalcMotorPowers(0, 1, 0)[1], 0.4);
        assertEquals(1, PID.nativeCalcMotorPowers(0, 1, 0)[2], 0.4);
        assertEquals(1, PID.nativeCalcMotorPowers(0, 1, 0)[3], 0.4);
        assertNotEquals(0, PID.nativeCalcMotorPowers(0, 1, 0)[0]);
        assertNotEquals(0, PID.nativeCalcMotorPowers(0, 1, 0)[1]);
        assertNotEquals(0, PID.nativeCalcMotorPowers(0, 1, 0)[2]);
        assertNotEquals(0, PID.nativeCalcMotorPowers(0, 1, 0)[3]);
    }
}
