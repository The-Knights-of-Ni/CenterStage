package org.theknightsofni.pidrs;

class PIDrs {
    // This declares that the static `hello` method will be provided
    // a native library.
    public static native String nativeRun();

    public static native double[] nativeCalcMotorPowers(double leftStickX, double leftStickY, double rightStickX);

    public static native double nativeClamp(double val, double min, double max);

    static {
        // This actually loads the shared object that we'll be creating.
        // The actual location of the .so or .dll may differ based on your
        // platform.
        System.loadLibrary("pidrs");
    }
}
