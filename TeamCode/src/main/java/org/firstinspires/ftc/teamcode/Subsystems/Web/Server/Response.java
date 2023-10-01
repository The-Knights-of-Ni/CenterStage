package org.firstinspires.ftc.teamcode.Subsystems.Web.Server;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Response {
    public int statusCode;
    public String statusMessage;
    public HashMap<String, String> headers;
    public byte[] body;

    public Response(int statusCode, String statusMessage, HashMap<String, String> headers, String body) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.headers = headers;
        char[] chars = body.toCharArray();
        this.body = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            this.body[i] = (byte) chars[i];
        }
    }

    public Response(int statusCode, String statusMessage, HashMap<String, String> headers, ByteArrayOutputStream body) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.headers = headers;
        this.body = body.toByteArray();
    }

    public String getHeaders() {
        return "HTTP/1.1 " + statusCode + " " + statusMessage + "\n" +
                headers.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n")) +
                "\n\n";
    }

    public byte[] toBytes() {
        byte[] headersBytes = getHeaders().getBytes();
        byte[] result = new byte[headersBytes.length + this.body.length];
        System.arraycopy(headersBytes, 0, result, 0, headersBytes.length);
        System.arraycopy(body, 0, result, headersBytes.length, body.length);
        return result;
    }
}
