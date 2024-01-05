package org.firstinspires.ftc.teamcode.Util;

import android.util.Log;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Web.WebLog;

public class MasterLogger {
    public Telemetry telemetry;
    String tag;

    public MasterLogger(Telemetry telemetry, String tag) {
        this.telemetry = telemetry;
        this.tag = tag;
    }

    public void error(String message) {
        if (telemetry != null) {
            telemetry.addData(tag, message);
            telemetry.update();
        }
        Log.e(tag, message);
        WebLog.error(tag, message);
    }

    public void error(String message, Object o) {
        if (telemetry != null) {
            telemetry.addData(tag, message, o);
            telemetry.update();
        }
        if (o.getClass().isInstance(Throwable.class)) {
            Log.e(tag, message, (Throwable) o);
        } else {
            Log.e(tag, message);
        }
        WebLog.error(tag, message, o);
    }

    public void warning(String message) {
        if (telemetry != null) {
            telemetry.addData(tag, message);
            telemetry.update();
        }
        Log.w(tag, message);
        WebLog.warning(tag, message);
    }

    public void warning(String message, Object o) {
        if (telemetry != null) {
            telemetry.addData(tag, message, o);
            telemetry.update();
        }
        if (o.getClass().isInstance(Throwable.class)) {
            Log.w(tag, message, (Throwable) o);
        } else {
            Log.w(tag, message);
        }
        WebLog.warning(tag, message, o);
    }

    public void info(String message) {
        if (telemetry != null) {
            telemetry.addData(tag, message);
            telemetry.update();
        }
        Log.i(tag, message);
        WebLog.info(tag, message);
    }

    public void info(String message, Object o) {
        if (telemetry != null) {
            telemetry.addData(tag, message, o);
            telemetry.update();
        }
        if (o.getClass().isInstance(Throwable.class)) {
            Log.i(tag, message, (Throwable) o);
        } else {
            Log.i(tag, message);
        }
        WebLog.info(tag, message, o);
    }

    public void debug(String message) {
        Log.d(tag, message);
        WebLog.debug(tag, message);
    }

    public void debug(String message, Object o) {
        if (o.getClass().isInstance(Throwable.class)) {
            Log.d(tag, message, (Throwable) o);
        } else {
            Log.d(tag, message);
        }
        WebLog.debug(tag, message, o);
    }

    public void verbose(String message) {
        Log.v(tag, message);
        WebLog.verbose(tag, message);
    }

    public void verbose(String message, Object o) {
        if (o.getClass().isInstance(Throwable.class)) {
            Log.v(tag, message, (Throwable) o);
        } else {
            Log.v(tag, message);
        }
        WebLog.verbose(tag, message, o);
    }
}
