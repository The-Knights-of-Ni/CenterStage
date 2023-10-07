package org.firstinspires.ftc.teamcode.Util;

import android.util.Log;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Web.WebLog;

public class MasterLogger {
    public Telemetry telemetry;
    String tag;

    public MasterLogger(Telemetry telemetry, String tag) {
        if (telemetry != null) {
            this.telemetry = telemetry;
        } else {
            this.telemetry = null;
        }
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

    public void error(String message, Throwable tr) {
        if (telemetry != null) {
            telemetry.addData(tag, message);
            telemetry.update();
        }
        Log.e(tag, message, tr);
        WebLog.error(tag, message);
    }

    public void warning(String message) {
        if (telemetry != null) {
            telemetry.addData(tag, message);
            telemetry.update();
        }
        Log.w(tag, message);
        WebLog.warning(tag, message);
    }

    public void info(String message) {
        Log.i(tag, message);
        WebLog.info(tag, message);
    }

    public void debug(String message) {
        Log.d(tag, message);
        WebLog.debug(tag, message);
    }

    public void verbose(String message) {
        Log.v(tag, message);
        WebLog.verbose(tag, message);
    }
}
