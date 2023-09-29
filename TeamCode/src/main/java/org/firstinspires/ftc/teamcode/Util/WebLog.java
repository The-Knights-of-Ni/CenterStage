package org.firstinspires.ftc.teamcode.Util;

import android.os.Build;

import java.time.LocalDate;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.timestamp = LocalDate.now().toString();
        }
        else {
            this.timestamp = "";
        }
    }
}
