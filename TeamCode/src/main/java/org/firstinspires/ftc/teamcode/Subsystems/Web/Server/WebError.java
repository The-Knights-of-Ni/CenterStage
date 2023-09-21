package org.firstinspires.ftc.teamcode.Subsystems.Web.Server;

public class WebError extends Exception {
    public String error;
    public transient int statusCode;

    public WebError(String error, int statusCode) {
        this.error = error;
        this.statusCode = statusCode;
    }
}
