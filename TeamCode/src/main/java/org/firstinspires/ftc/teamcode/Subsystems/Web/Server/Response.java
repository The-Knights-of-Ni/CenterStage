package org.firstinspires.ftc.teamcode.Subsystems.Web.Server;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.stream.Collectors;

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

    @NonNull
    @Override
    public String toString() {
        return "HTTP/1.1 " + statusCode + " " + statusMessage + "\n" +
                headers.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n")) +
                "\n\n" + body;
    }
}
