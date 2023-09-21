package org.firstinspires.ftc.teamcode.Util;

public class WebAction {
    String statusString;

    enum Status {
        SUCCESS,
        FAILURE,
        RUNNING
    }

    Status status;

    public WebAction(String status) {
        this.statusString = status;
        this.status = Status.RUNNING;
    }
}
