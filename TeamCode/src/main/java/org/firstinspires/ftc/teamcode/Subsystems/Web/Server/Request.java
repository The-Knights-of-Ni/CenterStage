package org.firstinspires.ftc.teamcode.Subsystems.Web.Server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Request {
    public String method;
    public String url;
    public String version;
    public HashMap<String, String> headers;


    public Request(String text) throws WebError {
        List<String> lines = Arrays.stream(text.split("\n")).collect(Collectors.toList());
        String topLine = lines.get(0);
        HashMap<String, String> h = new HashMap<>();
        for (String header : lines) {
            String[] split = header.split(": ?");
            if (split.length == 2 && !split[0].isEmpty() && !split[1].isEmpty()) {
                h.put(split[0], split[1]); // TODO: Handle duplicate headers
            } else {
                throw new WebError("Invalid header '" + header + "'", 400);
            }
        }
        String[] split = topLine.split(" ");
        if (split.length != 3) {
            throw new WebError("Invalid request (line 0: " + topLine + ")", 400);
        }
        this.method = split[0];
        this.url = split[1];
        this.version = split[2];
        this.headers = h;
    }

    public Request(String method, String url, String version, HashMap<String, String> headers) {
        this.method = method;
        this.url = url;
        this.version = version;
        this.headers = headers;
    }
}
