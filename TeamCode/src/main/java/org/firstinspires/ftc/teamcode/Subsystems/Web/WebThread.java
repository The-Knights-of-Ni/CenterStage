package org.firstinspires.ftc.teamcode.Subsystems.Web;

import com.google.gson.Gson;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Subsystems.Web.Canvas.WebCanvas;
import org.firstinspires.ftc.teamcode.Subsystems.Web.Server.Request;
import org.firstinspires.ftc.teamcode.Subsystems.Web.Server.Response;
import org.firstinspires.ftc.teamcode.Subsystems.Web.Server.WebError;
import org.firstinspires.ftc.teamcode.Util.Vector;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class WebThread extends Thread {

    private static final ArrayList<WebLog> logs = new ArrayList<>();
    private static final ArrayList<WebAction> actions = new ArrayList<>();
    public static Vector position = new Vector(0, 0);
    public static double theta = 0;
    private static final HashMap<String, String> defaultHeaders = new HashMap<>();
    int port;
    ServerSocket serverSocket;
    WebCanvas webCanvas;
    private final Gson gson;

    public WebThread() throws IOException {
        this(7070);
    }

    public WebThread(int port) throws IOException {
        gson = new Gson();
        this.port = port;
        this.webCanvas = new WebCanvas(500, 500);
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

    private Response returnError(WebError error) {
        return new Response(error.statusCode, "ERR", defaultHeaders, gson.toJson(error.toHashMap()));
    }

    private void invalidMethod(String method) throws WebError {
        throw new WebError("Method '" + method + "' not allowed", 405, 4050);
    }

    private Response returnObject(Object obj) {
        return new Response(200, "OK", defaultHeaders, gson.toJson(obj));
    }

    private Response handleRequest(Request req) throws WebError {
        if (Objects.equals(req.url, "/")) {
            if (Objects.equals(req.method, "GET")) {
                return returnObject(new View.MainResponse(logs, actions, position, theta));
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
                return returnObject(new View.RobotPos(position.getX(), position.getY(), theta));
            } else {
                invalidMethod(req.method);
            }
        } else if (Objects.equals(req.url, "/gamepads")) {
            if (Objects.equals(req.method, "GET")) {
                return returnObject(new View.GamepadResponse(Robot.gamepad1, Robot.gamepad2));
            } else {
                invalidMethod(req.method);
            }
        } else if (Objects.equals(req.url, "/canvas")) {
            if (Objects.equals(req.method, "GET")) {
                HashMap<String, String> headers = new HashMap<>(2);
                headers.put("Server", "Web Subsystem Thread");
                headers.put("Content-Type", "image/unknown");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                BufferedOutputStream bw = new BufferedOutputStream(stream);
                try {
                    bw.write(244);
                    bw.write(322);
                } catch (IOException e) {

                }
                return new Response(200, "OK", headers, stream);
            } else {
                invalidMethod(req.method);
            }
        }
        throw new WebError("Resource not found", 404, 4040);
    }

    /**
     * <p>Workflow:
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
                    output.write(resp.toBytes());
                    output.close();
                } catch (WebError e) {
                    OutputStream output = socket.getOutputStream();
                    Response resp = returnError(e);
                    output.write(resp.toBytes());
                    output.close();
                } catch (Exception e) {
                    OutputStream output = socket.getOutputStream();
                    Response resp = returnError(new WebError(e.getMessage(), 500, 5000));
                    output.write(resp.toBytes());
                    output.close();
                    System.out.println("Unhandled Error on WebThread, graceful exit performed: " + e.getMessage());
                }
            } catch (Exception e) {
                System.out.println("Unhandled Error on WebThread, hard exit: " + e.getMessage());
            }
        }
    }
}
