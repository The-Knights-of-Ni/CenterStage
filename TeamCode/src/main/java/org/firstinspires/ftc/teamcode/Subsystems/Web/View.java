package org.firstinspires.ftc.teamcode.Subsystems.Web;

import org.firstinspires.ftc.teamcode.GamepadWrapper;
import org.firstinspires.ftc.teamcode.Util.Pose;

import java.util.ArrayList;

public class View {

    public static class MainResponse {
        public ArrayList<WebLog> logs;

        public ArrayList<WebAction> actions;

        public Pose position;

        public MainResponse(ArrayList<WebLog> logs, ArrayList<WebAction> actions, Pose position) {
            this.logs = logs;
            this.actions = actions;
            this.position = position;
        }
    }

    public static class GamepadResponse {
        public GamepadWrapper gamepad1;
        public GamepadWrapper gamepad2;

        public GamepadResponse(GamepadWrapper gamepad1, GamepadWrapper gamepad2) {
            this.gamepad1 = gamepad1;
            this.gamepad2 = gamepad2;
        }
    }
}
