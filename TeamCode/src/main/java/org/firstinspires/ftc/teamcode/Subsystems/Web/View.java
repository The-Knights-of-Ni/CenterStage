package org.firstinspires.ftc.teamcode.Subsystems.Web;

import org.firstinspires.ftc.teamcode.GamepadWrapper;
import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.ArrayList;

public class View {
    public static class RobotPos {
        public double x;
        public double y;

        public double theta;

        public RobotPos(double x, double y, double theta) {
            this.x = x;
            this.y = y;
            this.theta = theta;
        }
    }

    public static class MainResponse {
        public ArrayList<WebLog> logs;

        public ArrayList<WebAction> actions;

        public RobotPos position;

        public MainResponse(ArrayList<WebLog> logs, ArrayList<WebAction> actions, Vector position, double theta) {
            this.logs = logs;
            this.actions = actions;
            this.position = new RobotPos(position.getX(), position.getY(), theta);
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
