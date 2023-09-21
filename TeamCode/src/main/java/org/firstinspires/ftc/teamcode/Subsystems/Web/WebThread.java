package org.firstinspires.ftc.teamcode.Subsystems.Web;

import org.firstinspires.ftc.teamcode.GamepadWrapper;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Subsystems.Web.Server.Request;
import org.firstinspires.ftc.teamcode.Subsystems.Web.Server.Response;
import org.firstinspires.ftc.teamcode.Subsystems.Web.Server.WebError;
import org.firstinspires.ftc.teamcode.Util.Vector;

import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class WebThread extends Thread {

    private static HashMap<String, String> defaultHeaders = new HashMap<>();

    private Gson gson;

    class RobotPos {
        public double x;
        public double y;

        public RobotPos(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    class MainResponse {
        public ArrayList<WebLog> logs;

        public ArrayList<WebAction> actions;

        public RobotPos position;

        public MainResponse(ArrayList<WebLog> logs, ArrayList<WebAction> actions, Vector position) {
            this.logs = logs;
            this.actions = actions;
            this.position = new RobotPos(position.getX(), position.getY());
        }
    }

    class GamepadResponse {
        public GamepadWrapper gamepad1;
        public GamepadWrapper gamepad2;

        public GamepadResponse(GamepadWrapper gamepad1, GamepadWrapper gamepad2) {
            this.gamepad1 = gamepad1;
            this.gamepad2 = gamepad2;
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


    private Response returnError(WebError error) {
        return new Response(error.statusCode, "ERR", defaultHeaders, gson.toJson(error.toHashMap()));
    }

    private void invalidMethod(String method) throws WebError {
        throw new WebError("Method '" + method + "' not allowed", 405);
    }

    private Response returnObject(Object obj) {
        return new Response(200, "OK", defaultHeaders, gson.toJson(obj));
    }

    private Response handleRequest(Request req) throws WebError {
        if (Objects.equals(req.url, "/")) {
            if (Objects.equals(req.method, "GET")) {
                return returnObject(new MainResponse(logs, actions, position));
            } else {
                invalidMethod(req.method);
            }
        } else if (Objects.equals(req.url, "/logs")) {
            if (Objects.equals(req.method, "GET")) {
                return returnObject(logs);
            } else {
                invalidMethod(req.method);
            }
        } else if (Objects.equals(req.url, "/actions")) {
            if (Objects.equals(req.method, "GET")) {
                return returnObject(actions);
            } else {
                invalidMethod(req.method);
            }
        } else if (Objects.equals(req.url, "/position")) {
            if (Objects.equals(req.method, "GET")) {
                return returnObject(new RobotPos(position.getX(), position.getY()));
            } else {
                invalidMethod(req.method);
            }
        } else if (Objects.equals(req.url, "/gamepads")) {
            if (Objects.equals(req.method, "GET")) {
                return returnObject(new GamepadResponse(Robot.gamepad1, Robot.gamepad2));
            } else {
                invalidMethod(req.method);
            }
        }

        throw new WebError("Resource not found", 404);
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

    /** <p>Workflow:
     * <p>- Read socket to end
     * <p>- Parse request ({@link Request#Request(String)})
     * <p>- Generate response ({@link WebThread#handleRequest(Request)})
     * <p>- Return response
     */
    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                InputStreamReader reader = new InputStreamReader(socket.getInputStream());
                String inputString = readToEnd(reader);
                try {
                    Request req = new Request(inputString);
                    System.out.println(req.method + " " + req.url + " " + socket.getInetAddress().getHostAddress());
                    Response resp = handleRequest(req);
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
                    writer.println(resp); // Automatic string conversion {@link Response#toString()}
                    output.close();
                } catch (WebError e) {
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);
                    Response resp = returnError(e);
                    writer.println(resp); // Automatic string conversion {@link Response#toString()}
                    output.close();
                }
            } catch (Exception e) {
                System.out.println("Error on WebThread: " + e.getMessage());
            }
        }
    }
}
