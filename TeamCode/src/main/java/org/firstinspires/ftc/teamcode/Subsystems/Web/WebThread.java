package org.firstinspires.ftc.teamcode.Subsystems.Web;

import android.util.Log;
import org.firstinspires.ftc.teamcode.Util.Vector;

import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;


public class WebThread extends Thread {
    private Gson gson;

    class MainResponse {
        public ArrayList<WebLog> logs;
        public ArrayList<WebAction> actions;

        public class RobotPos {
            public double x;
            public double y;

            public RobotPos(double x, double y) {
                this.x = x;
                this.y = y;
            }
        }

        public RobotPos position;

        public MainResponse(ArrayList<WebLog> logs, ArrayList<WebAction> actions, Vector position) {
            this.logs = logs;
            this.actions = actions;
            this.position = new RobotPos(position.getX(), position.getY());
        }
    }

    private static final ArrayList<WebLog> logs = new ArrayList<>();
    public static final ArrayList<WebAction> actions = new ArrayList<>();

    public static org.firstinspires.ftc.teamcode.Util.Vector position = new Vector(0, 0);
    int port;
    ServerSocket serverSocket;

    public WebThread() throws IOException {
        gson = new Gson();
        port = 7070;
        serverSocket = new ServerSocket(port);
    }

    public WebThread(int port) throws IOException {
        gson = new Gson();
        this.port = port;
        serverSocket = new ServerSocket(port);
    }

    public static void addLog(WebLog log) {
        logs.add(log);
    }

    public static void setPercentage(String task, int percentage) {
        for (WebAction action : actions) {
            if (Objects.equals(action.name, task)) {
                if (percentage == 100) {
                    action.status = WebAction.Status.SUCCESS;
                } else {
                    action.status = WebAction.Status.RUNNING;
                }
                action.progress = percentage;
            }
        }
    }

    public static void setPercentage(String task, int progress, int total) {
        setPercentage(task, (Math.abs(progress) / Math.abs(total)) * 100);
    }

    public static void removeAction(String task) {
        actions.removeIf(action -> Objects.equals(action.name, task));
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
                System.out.println("WebThread: " + request);
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
                    resp = gson.toJson(new MainResponse(logs, actions, position));
                } else {
                    statusCode = 404;
                    resp = "{\"error\": \"Resource not Found.\"}";
                }
                writer.println("HTTP/1.1 " + statusCode + " OK\n" +
                        "Server: Web Subsystem v0.0.0\n" +
                        "Content-Type: application/json\n" +
                        "\n\n" + resp);
                output.close();
            } catch (Exception e) {
                System.out.println("ERROR ON WebThread: " + e.getMessage());
            }
        }
    }
}
