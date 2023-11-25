package org.knightsofni.visionrs;

public class NativeVision {
    // This declares that the static `hello` method will be provided
    // a native library.
    public static native byte process();

    static {
        // This actually loads the shared object that we'll be creating.
        // The actual location of the .so or .dll may differ based on your
        // platform.
        System.loadLibrary("visionrs");
    }
}
