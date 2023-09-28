package org.firstinspires.ftc.teamcode.Subsystems.Web.Server;

import java.util.HashMap;

public class WebError extends Exception {
    public String error;
    public int statusCode;
    public int errorCode;

    public WebError(String error, int statusCode, int errorCode) {
        this.error = error;
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }

    public HashMap<String, HashMap<String, Object>> toHashMap() {
        HashMap<String, HashMap<String, Object>> map = new HashMap<>(1);
        HashMap<String, Object> errorDetails = new HashMap<>(2);
        errorDetails.put("message", error);
        errorDetails.put("code", errorCode);
        map.put("error", errorDetails);
        return map;
    }
}
