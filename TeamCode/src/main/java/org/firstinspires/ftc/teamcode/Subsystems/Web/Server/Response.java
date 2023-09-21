package org.firstinspires.ftc.teamcode.Subsystems.Web.Server;

import java.util.HashMap;

public class Response {
    public int statusCode;
    public String statusMessage;
    public HashMap<String, String> headers;
    public String body;

    public Response(int statusCode, String statusMessage, HashMap<String, String> headers, String body) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.headers = headers;
        this.body = body;
    }
}
