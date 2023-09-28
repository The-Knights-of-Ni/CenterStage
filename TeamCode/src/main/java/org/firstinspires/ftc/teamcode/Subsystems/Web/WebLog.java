package org.firstinspires.ftc.teamcode.Subsystems.Web;

import android.os.Build;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WebLog {
    public enum LogSeverity {
        VERBOSE,
        DEBUG,
        INFO,
        WARNING,
        ERROR
    }

    public String TAG;
    public String message;
    public LogSeverity severity;
    public String timestamp;

    public WebLog(String tag, String message, LogSeverity severity) {
        this.TAG = tag;
        this.message = message;
        this.severity = severity;
        this.timestamp = ((Long) System.currentTimeMillis()).toString();
    }

    public static void verbose(String tag, String message) {
        WebThread.addLog(new WebLog(tag, message, LogSeverity.VERBOSE));
    }

    public static void debug(String tag, String message) {
        WebThread.addLog(new WebLog(tag, message, LogSeverity.DEBUG));
    }

    public static void info(String tag, String message) {
        WebThread.addLog(new WebLog(tag, message, LogSeverity.INFO));
    }

    public static void warning(String tag, String message) {
        WebThread.addLog(new WebLog(tag, message, LogSeverity.WARNING));
    }

    public static void error(String tag, String message) {
        WebThread.addLog(new WebLog(tag, message, LogSeverity.ERROR));
    }
}
