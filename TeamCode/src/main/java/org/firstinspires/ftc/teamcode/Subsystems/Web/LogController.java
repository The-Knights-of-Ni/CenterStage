package org.firstinspires.ftc.teamcode.Subsystems.Web;

import org.firstinspires.ftc.teamcode.Util.WebLog;

import java.util.ArrayList;

public class LogController {
    WebThreadData wtd;

    public LogController() {
        this.wtd = WebThreadData.getWtd();
    }

    public String getAll() {
        StringBuilder json = new StringBuilder("{");
        ArrayList<WebLog> logs = wtd.getLogs();
        for (WebLog log: logs) {
            json.append("\n{\n" + "\"tag\": ").append(log.TAG).append(",\n\"message\": ").append(log.message).append(",\n\"severity\": ").append(log.severity).append("\n},");
        }
        json = new StringBuilder(json.substring(0, json.length() - 2));
        json.append("}");
        return json.toString();
    }
}
