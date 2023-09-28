package org.theknightsofni.pidrs;

class PID {
    // This declares that the static `hello` method will be provided
    // a native library.
    private static native String nativeRun();

    static {
        // This actually loads the shared object that we'll be creating.
        // The actual location of the .so or .dll may differ based on your
        // platform.
        System.loadLibrary("pidrs");
    }
}
