package org.firstinspires.ftc.teamcode.Subsystems.Web;

import java.time.LocalDateTime;

public class WebAction { // TODO: Make autocloseable or something
    public String statusString;
    public int progress;

    public enum Status {
        SUCCESS,
        FAILURE,
        RUNNING
    }

    public String name;
    public Status status;
    public String startTimestamp;

    public WebAction(String name, String status) {
        this.name = name;
        this.statusString = status;
        this.progress = 0;
        this.status = Status.RUNNING;
        this.startTimestamp = LocalDateTime.now().toString();
    }
}
