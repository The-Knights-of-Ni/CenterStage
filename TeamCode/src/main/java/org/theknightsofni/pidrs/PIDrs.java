package org.theknightsofni.pidrs;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class PIDrs {
    // This declares that the static `hello` method will be provided
    // a native library.
    public static native void nativeRun(DcMotorEx frontLeft, DcMotorEx frontRight, DcMotorEx rightLeft, DcMotorEx rightRight);

    public static native int[] nativeCalcMotorDistances(double x, double y, double turnAngle,
                                                        double COUNTS_CORRECTION_X,
                                                        double COUNTS_CORRECTION_Y,
                                                        double COUNTS_PER_MM,
                                                        double COUNTS_PER_DEGREE);

    public static native double[] nativeCalcMotorPowers(double leftStickX, double leftStickY, double rightStickX);

    static {
        // This actually loads the shared object that we'll be creating.
        // The actual location of the .so or .dll may differ based on your
        // platform.
        System.loadLibrary("pidrs");
    }
}
