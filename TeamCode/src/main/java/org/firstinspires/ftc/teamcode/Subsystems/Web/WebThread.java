package org.firstinspires.ftc.teamcode.Subsystems.Web;

import android.util.JsonWriter;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.Util.WebLog;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class WebThread extends Subsystem implements Runnable {
    WebThreadData wtd = WebThreadData.getWtd();
//    Javalin app;
    int port;

    public WebThread(Telemetry telemetry) {
        super(telemetry, "web");
        init();
        port = 7070;
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
            json.append("\n{\n" + "\"tag\": ").append(log.TAG).append(",\n\"message\": ").append(log.message).append(",\n\"severity\": ").append(log.severity).append("\n},");
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
//        app.start(port);
    }
}
