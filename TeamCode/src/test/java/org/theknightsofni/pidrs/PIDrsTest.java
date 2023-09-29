package org.theknightsofni.pidrs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PIDrsTest {
    @Test
    void testInit() {
        PIDrs.nativeRun();
    }

    @Test
    void testCalcMotorPower2D() {
        assertEquals(1, PIDrs.nativeCalcMotorPowers(0, 1, 0)[0], 0.4);
        assertEquals(1, PIDrs.nativeCalcMotorPowers(0, 1, 0)[1], 0.4);
        assertEquals(1, PIDrs.nativeCalcMotorPowers(0, 1, 0)[2], 0.4);
        assertEquals(1, PIDrs.nativeCalcMotorPowers(0, 1, 0)[3], 0.4);
        assertNotEquals(0, PIDrs.nativeCalcMotorPowers(0, 1, 0)[0]);
        assertNotEquals(0, PIDrs.nativeCalcMotorPowers(0, 1, 0)[1]);
        assertNotEquals(0, PIDrs.nativeCalcMotorPowers(0, 1, 0)[2]);
        assertNotEquals(0, PIDrs.nativeCalcMotorPowers(0, 1, 0)[3]);
    }

    @Test
    void testClampRS() {
        assertEquals(0.5, PIDrs.nativeClamp(0.5, -1, 1));
        assertEquals(1, PIDrs.nativeClamp(4, -1, 1));
        assertEquals(-1, PIDrs.nativeClamp(-4, -1, 1));
    }
}
