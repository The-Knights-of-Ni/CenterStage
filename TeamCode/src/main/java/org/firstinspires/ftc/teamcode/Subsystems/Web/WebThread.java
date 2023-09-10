package org.firstinspires.ftc.teamcode.Subsystems.Web;

import android.util.JsonWriter;

import android.util.Log;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.Util.WebLog;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


public class WebThread extends Subsystem implements Runnable {
    WebThreadData wtd = WebThreadData.getWtd();
    int port;
    ServerSocket serverSocket;

    public WebThread(Telemetry telemetry) throws IOException {
        super(telemetry, "web");
        init();
        port = 7070;
        serverSocket = new ServerSocket(port);
    }

    public WebThread(Telemetry telemetry, int port) {
        super(telemetry, "web");
        init();
        this.port = port;
    }

    private void init() {
//        app = Javalin.create(config -> {
//        });
//        app.get("/logs", ctx -> ctx.result(getLogs()));
//        app.get("/", ctx -> ctx.result(
//                "{\n" +
//                "    \"version\": \"0.0.0\"" +
//                "\n}"));
//        app.get("/robot-position", ctx -> ctx.result(getRobotPosition()));

    }


    public String writeJson(ArrayList<WebLog> logs) throws IOException {
        OutputStream output = new OutputStream() {
            private StringBuilder string = new StringBuilder();

            @Override
            public void write(int b) throws IOException {
                this.string.append((char) b);
            }

            @Override
            public String toString() {
                return this.string.toString();
            }
        };
        writeJsonStream(output, logs);
        return output.toString();
    }

    public void writeJsonStream(OutputStream out, ArrayList<WebLog> logs) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        writer.setIndent("    ");
        writeMessagesArray(writer, logs);
        writer.close();
    }

    public void writeMessagesArray(JsonWriter writer, ArrayList<WebLog> messages) throws IOException {
        writer.beginArray();
        for (WebLog log : messages) {
            writeMessage(writer, log);
        }
        writer.endArray();
    }

    public void writeMessage(JsonWriter writer, WebLog webLog) throws IOException {
        writer.beginObject();
        writer.name("tag").value(webLog.TAG);
        writer.name("message").value(webLog.message);
        writer.name("severity").value(webLog.severity.ordinal());
        writer.endObject();
    }


    public String getLogs() {
        StringBuilder json = new StringBuilder("{");
        ArrayList<WebLog> logs = wtd.getLogs();
        for (WebLog log: logs) {
            json.append("\n{\n" + "\"tag\": ")
                    .append(log.TAG)
                    .append(",\n\"message\": ")
                    .append(log.message)
                    .append(",\n\"severity\": ")
                    .append(log.severity)
                    .append("\n},");
        }
        json = new StringBuilder(json.substring(0, json.length() - 2));
        json.append("}");
        return json.toString();
    }

    public String getRobotPosition() {
        return "{\n\"x\": " + wtd.getPosition().getX() + ",\n\"y\": " + wtd.getPosition().getY();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                InputStream input = socket.getInputStream();
                InputStreamReader reader = new InputStreamReader(input);
                StringBuilder str = new StringBuilder();
                boolean exit = false;
                int prev = 0;
                while (!exit) {
                    int result = reader.read();
                    if (result == -1) {
                        exit = true;
                    }
                    else if (result == 13 && prev == 10) {
                        exit = true;
                    }
                    else {
                        str.append((char) result);
                    }
                    prev = result;
                }
                List<String> lines = Arrays.stream(str.toString().split("\n")).collect(Collectors.toList());
                String request = lines.get(0);
                String[] topSplit = request.split(" ");
                String method = topSplit[0];
                String url = topSplit[1];
                String version = topSplit[2];
                Log.i("WebThread", request);
                lines.remove(0);
                HashMap<String, String> headers = new HashMap<>();
                for (String header: lines) {
                    String[] split = header.split(":( )");
                    headers.put(split[0], split[1]);
                }
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                int statusCode = 200;
                String resp = "";
                if (Objects.equals(url, "/")) {
                    resp = getLogs();
                } else if (Objects.equals(url, "/robot-position")) {
                    resp = getRobotPosition();
                }
                else {
                    statusCode = 404;
                    resp = "{\"error\": \"Resource not Found.\"";
                }
                writer.println("HTTP/1.1 " + statusCode + " OK\n" +
                        "Server: v0.0.0\n" +
                        "Content-Type: text/json\n" +
                        "\n\n" + resp);
                output.close();
            } catch (Exception e) {
                Log.e("WebThread", e.toString());
            }
        }
    }
}
