package org.theknightsofni.pidrs;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Drive;
import org.firstinspires.ftc.teamcode.Util.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PIDrsTest {
    @Test
    void testInit() {
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
    void testMotorDistanceRS() {
        assertEquals(1782, PIDrs.nativeCalcMotorDistances(0, 1000, 0,
                Drive.COUNTS_CORRECTION_X, Drive.COUNTS_CORRECTION_Y, Drive.COUNTS_PER_MM, Drive.COUNTS_PER_DEGREE)[0]);
        assertEquals(1782, PIDrs.nativeCalcMotorDistances(0, 1000, 0,
                Drive.COUNTS_CORRECTION_X, Drive.COUNTS_CORRECTION_Y, Drive.COUNTS_PER_MM, Drive.COUNTS_PER_DEGREE)[1]);
        assertEquals(1782, PIDrs.nativeCalcMotorDistances(0, 1000, 0,
                Drive.COUNTS_CORRECTION_X, Drive.COUNTS_CORRECTION_Y, Drive.COUNTS_PER_MM, Drive.COUNTS_PER_DEGREE)[2]);
        assertEquals(1782, PIDrs.nativeCalcMotorDistances(0, 1000, 0,
                Drive.COUNTS_CORRECTION_X, Drive.COUNTS_CORRECTION_Y, Drive.COUNTS_PER_MM, Drive.COUNTS_PER_DEGREE)[3]);
    }

    @Test
    void benchMotorDistanceJava() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            int turnAngle = 0;
            Vector newV = new Vector(0 * Drive.COUNTS_CORRECTION_X * Drive.COUNTS_PER_MM, 1000 * Drive.COUNTS_CORRECTION_Y * Drive.COUNTS_PER_MM);
            // Sqrt2 is introduced as a correction factor, since the pi/4 in the next line is required
            // for the strafer chassis to operate properly
            double distance = newV.distance(Vector2D.ZERO) * Math.sqrt(2);
            double angle = Math.atan2(newV.getY(), newV.getX()) - Math.PI / 4;

            int[] tickCount = new int[4]; // All tick counts need to be integers
            tickCount[0] = (int) ((distance * Math.cos(angle)));
            tickCount[0] -= (int) (turnAngle * Drive.COUNTS_PER_DEGREE);
            tickCount[1] = (int) ((distance * Math.sin(angle)));
            tickCount[1] += (int) (turnAngle * Drive.COUNTS_PER_DEGREE);
            tickCount[2] = (int) ((distance * Math.sin(angle)));
            tickCount[2] -= (int) (turnAngle * Drive.COUNTS_PER_DEGREE);
            tickCount[3] = (int) ((distance * Math.cos(angle)));
            tickCount[3] += (int) (turnAngle * Drive.COUNTS_PER_DEGREE);
        }
        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end - start) + " milliseconds");
    }

    @Test
    void benchMotorDistanceRS() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            PIDrs.nativeCalcMotorDistances(0, 1000, 0,
                    Drive.COUNTS_CORRECTION_X, Drive.COUNTS_CORRECTION_Y, Drive.COUNTS_PER_MM, Drive.COUNTS_PER_DEGREE);
        }
        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end - start) + " milliseconds");
    }
}
