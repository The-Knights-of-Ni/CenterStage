package org.firstinspires.ftc.teamcode.Subsystems.Web;

public record WebLog(String tag, String message, LogSeverity severity, String timestamp, Object data) {
    public WebLog(String tag, String message, LogSeverity severity, Object data) {
        this(tag, message, severity, ((Long) System.currentTimeMillis()).toString(), data);
    }

    public WebLog(String tag, String message, LogSeverity severity) {
        this(tag, message, severity, null);
    }

    public static void verbose(String tag, String message) {
        verbose(tag, message, null);
    }

    public static void verbose(String tag, String message, Object object) {
        WebThread.addLog(new WebLog(tag, message, LogSeverity.VERBOSE, object));
    }

    public static void debug(String tag, String message) {
        debug(tag, message, null);
    }

    public static void debug(String tag, String message, Object object) {
        WebThread.addLog(new WebLog(tag, message, LogSeverity.DEBUG, object));
    }

    public static void info(String tag, String message) {
        info(tag, message, null);
    }

    public static void info(String tag, String message, Object object) {
        WebThread.addLog(new WebLog(tag, message, LogSeverity.INFO, object));
    }

    public static void warning(String tag, String message) {
        warning(tag, message, null);
    }

    public static void warning(String tag, String message, Object object) {
        WebThread.addLog(new WebLog(tag, message, LogSeverity.WARNING, object));
    }

    public static void error(String tag, String message) {
        error(tag, message, null);
    }

    public static void error(String tag, String message, Object object) {
        WebThread.addLog(new WebLog(tag, message, LogSeverity.ERROR, object));
    }

    public enum LogSeverity {
        VERBOSE,
        DEBUG,
        INFO,
        WARNING,
        ERROR
    }
}
