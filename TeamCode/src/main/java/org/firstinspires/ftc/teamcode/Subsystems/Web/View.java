package org.firstinspires.ftc.teamcode.Subsystems.Web;

import org.firstinspires.ftc.teamcode.GamepadWrapper;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Drive;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.PIDCoefficients;
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

    public static class DriveResponse {
        public double mmPerInch;
        public double countsPerInch;
        public double purePursuitLookaheadDistance;
        public PIDCoefficients xyPIDCoefficients;
        public PIDCoefficients thetaPIDCoefficients;

        public DriveResponse() {
            this.mmPerInch = Drive.mmPerInch;
            this.countsPerInch = Drive.COUNTS_PER_INCH;
            this.purePursuitLookaheadDistance = Drive.PURE_PURSUIT_LOOKAHEAD_DISTANCE;
            this.xyPIDCoefficients = Drive.xyPIDCoefficients;
            this.thetaPIDCoefficients = Drive.thetaPIDCoefficients;
        }
    }
}
