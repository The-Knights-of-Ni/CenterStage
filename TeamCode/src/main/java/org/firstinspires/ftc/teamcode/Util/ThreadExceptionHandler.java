package org.firstinspires.ftc.teamcode.Util;

import android.util.Log;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.lang.Thread.UncaughtExceptionHandler;

public class ThreadExceptionHandler implements UncaughtExceptionHandler {

    Telemetry telemetry;

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        telemetry.addData("Thread Death Message: ", "Caught Exception on Thread " + t.getClass().getName() + "\nThread shutting down\nSee logcat for details");
        Log.e("main", "Caught Exception on Thread " + t.getId() + " " + t.getClass().getName() + "\nThread shutting down", e);
        t.interrupt();
        if (t.isAlive()) {
            t.interrupt();
        }
    }
}
