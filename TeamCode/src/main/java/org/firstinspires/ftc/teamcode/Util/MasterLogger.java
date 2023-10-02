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
        telemetry.addData(tag, message);
        telemetry.update();
        Log.e(tag, message);
        WebLog.error(tag, message);
    }

    public void warning(String message) {
        telemetry.addData(tag, message);
        telemetry.update();
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

    public void e(String message) {
        error(message);
    }

    public void w(String message) {
        warning(message);
    }

    public void i(String message) {
        info(message);
    }

    public void d(String message) {
        debug(message);
    }

    public void v(String message) {
        verbose(message);
    }


}
