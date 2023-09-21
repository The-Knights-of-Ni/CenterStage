package org.firstinspires.ftc.teamcode.Subsystems.Web.Server;

import java.util.HashMap;

public class WebError extends Exception {
    public String error;
    public transient int statusCode;

    public WebError(String error, int statusCode) {
        this.error = error;
        this.statusCode = statusCode;
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("error", error);
        return map;
    }
}
