package org.firstinspires.ftc.teamcode.Subsystems.Web;

public class WebAction {
    public String statusString;
    public int progress;

    public enum Status {
        SUCCESS,
        FAILURE,
        RUNNING
    }

    public String name;
    public Status status;

    public WebAction(String name, String status) {
        this.name = name;
        this.statusString = status;
        this.progress = 0;
        this.status = Status.RUNNING;
    }
}
