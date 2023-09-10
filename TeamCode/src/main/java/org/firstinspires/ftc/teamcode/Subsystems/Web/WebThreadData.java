package org.firstinspires.ftc.teamcode.Subsystems.Web;

import org.firstinspires.ftc.teamcode.Util.Coordinate;
import org.firstinspires.ftc.teamcode.Util.WebLog;

import java.util.ArrayList;

public class WebThreadData {
    private static WebThreadData wtd;
    private static final ArrayList<WebLog> logs = new ArrayList<>();

    private static Coordinate position = new Coordinate(0,0);

    public static WebThreadData getWtd() {
        return wtd;
    }

    public static ArrayList<WebLog> getLogs() {
        return logs;
    }

    public void addLog(WebLog log) {
        wtd.logs.add(log);
    }

    public static Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        wtd.position = position;
    }
}
