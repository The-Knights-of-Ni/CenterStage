package org.firstinspires.ftc.teamcode.Subsystems.Web.Server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Request {
    private static final Pattern HEADER_PATTERN = Pattern.compile(": ?");
    public String method;
    public String url;
    public String version;
    public HashMap<String, String> headers;

    public String data;


    public Request(String text) throws WebError {
        List<String> lines = Arrays.stream(text.split("\n")).collect(Collectors.toList());
        String topLine = lines.get(0);
        lines.remove(0);
        HashMap<String, String> h = new HashMap<>();
        boolean parsingHeaders = true;
        StringBuilder body = new StringBuilder();
        for (String header : lines) {
            if (parsingHeaders) {
                String[] split = HEADER_PATTERN.split(header);
                if (split.length > 1 && !split[0].isEmpty() && !split[1].isEmpty()) {
                    h.put(split[0], split[1]); // TODO: Handle duplicate headers
                    // TODO: Handle headers with multiple semicolons
                } else if (header.isEmpty()) {
                    parsingHeaders = false;
                } else {
                    throw new WebError("Invalid header '" + header + "'", 400, 4001);
                }
            } else {
                body.append(header).append("\n");
            }
        }
        String[] split = topLine.split(" ");
        if (split.length != 3) {
            throw new WebError("Invalid request (line 0: " + topLine + ")", 400, 4000);
        }
        this.method = split[0];
        this.url = split[1];
        this.version = split[2];
        this.headers = h;
        this.data = body.toString();
    }

    public Request(String method, String url, String version, HashMap<String, String> headers, String data) {
        this.method = method;
        this.url = url;
        this.version = version;
        this.headers = headers;
        this.data = data;
    }
}
