package org.firstinspires.ftc.teamcode.Subsystems.Web;

import android.util.Log;
import org.firstinspires.ftc.teamcode.Util.Vector;
import org.firstinspires.ftc.teamcode.Util.WebLog;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;


public class WebThread extends Thread {
    public static final ArrayList<WebLog> logs = new ArrayList<>();

    public static org.firstinspires.ftc.teamcode.Util.Vector position = new Vector(0, 0);
    int port;
    ServerSocket serverSocket;

    public WebThread() throws IOException {
        port = 7070;
        serverSocket = new ServerSocket(port);
    }

    public WebThread(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port);
    }

    public String getLogs() {
        StringBuilder json = new StringBuilder("{ \"logs\": [");
        for (WebLog log : logs) {
            json.append("\n{\n" + "\"tag\": ")
                    .append(log.TAG)
                    .append(",\n\"message\": ")
                    .append(log.message)
                    .append(",\n\"severity\": ")
                    .append(log.severity)
                    .append("\n},");
        }
        json = new StringBuilder(json.substring(0, json.length() - 2));
        json.append("]\n}");
        return json.toString();
    }

    public String getRobotPosition() {
        return "{\n\"x\": " + position.getX() + ",\n\"y\": " + position.getY();
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
                    } else if (result == 13 && prev == 10) {
                        exit = true;
                    } else {
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
                for (String header : lines) {
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
                } else {
                    statusCode = 404;
                    resp = "{\"error\": \"Resource not Found.\"}";
                }
                writer.println("HTTP/1.1 " + statusCode + " OK\n" +
                        "Server: Web SubsystemW v0.0.0\n" +
                        "Content-Type: text/json\n" +
                        "\n\n" + resp);
                output.close();
            } catch (Exception e) {
                Log.e("WebThread", e.toString());
            }
        }
    }
}
