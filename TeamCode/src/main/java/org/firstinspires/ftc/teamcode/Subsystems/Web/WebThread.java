package org.firstinspires.ftc.teamcode.Subsystems.Web;

import org.firstinspires.ftc.teamcode.Subsystems.Web.Server.Request;
import org.firstinspires.ftc.teamcode.Subsystems.Web.Server.Response;
import org.firstinspires.ftc.teamcode.Subsystems.Web.Server.WebError;
import org.firstinspires.ftc.teamcode.Util.Vector;

import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;


public class WebThread extends Thread {

    private static HashMap<String, String> defaultHeaders = new HashMap<>();

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
    private static final ArrayList<WebAction> actions = new ArrayList<>();

    public static org.firstinspires.ftc.teamcode.Util.Vector position = new Vector(0, 0);
    int port;
    ServerSocket serverSocket;

    public WebThread() throws IOException {
        this(7070);
    }

    public WebThread(int port) throws IOException {
        gson = new Gson();
        this.port = port;
        serverSocket = new ServerSocket(port);
        defaultHeaders.put("Content-Type", "application/json");
        defaultHeaders.put("Server", "Web Subsystem Thread");
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

    public static void addAction(WebAction action) {
        actions.add(action);
    }

    public static void removeAction(String task) {
        actions.removeIf(action -> Objects.equals(action.name, task));
    }


    private static String renderResponse(Response resp) {
        return "HTTP/1.1 " + resp.statusCode + " " + resp.statusMessage + "\n" +
                resp.headers.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n")) +
                "\n\n" + resp.body;
    }

    private Response returnError(WebError error) {
        return new Response(error.statusCode, "ERR", defaultHeaders, gson.toJson(error));
    }

    private Response handleRequest(Request req) throws WebError {
        if (Objects.equals(req.url, "/")) {
            if (Objects.equals(req.method, "GET")) {
                return new Response(200, "OK", defaultHeaders, gson.toJson(new MainResponse(logs, actions, position)));
            } else {
                throw new WebError("Method '" + req.method + "' not allowed for endpoint '/'. Use GET instead.", 405);
            }
        } else {
            throw new WebError("Resource not found", 404);
        }
    }

    private static String readToEnd(InputStreamReader reader) throws IOException {
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
        return str.toString();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                InputStreamReader reader = new InputStreamReader(socket.getInputStream());
                String inputString = readToEnd(reader);
                try {
                    Request r = new Request(inputString);
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
                    Response resp = handleRequest(r);
                    writer.println(renderResponse(resp));
                    output.close();
                } catch (WebError e) {
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
                    Response resp = returnError(e);
                    writer.println(renderResponse(resp));
                    output.close();
                }
            } catch (Exception e) {
                System.out.println("ERROR ON WebThread: " + e.getMessage());
            }
        }
    }
}
