package org.firstinspires.ftc.teamcode.Testop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Subsystems.Control.Control;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;

import java.io.IOException;
import java.util.HashMap;

@TeleOp(name = "Servo Test", group= "Concept")
public class ServoTest extends LinearOpMode {
    private Robot robot;
    public ElapsedTime timer;
    double absIncrementStep = 0.005;

    private void initOpMode() throws IOException {
        telemetry.addData("Init Robot", "");
        telemetry.update();
        timer = new ElapsedTime();
        HashMap<String, Boolean> flags = new HashMap<String, Boolean>();
        flags.put("vision", false);
        this.robot =  new Robot(hardwareMap, telemetry, timer, AllianceColor.BLUE, gamepad1, gamepad2, flags);

        robot.control.initDevicesTeleop();
        robot.telemetryBroadcast("wait for start", "");
    }

    @Override
    public void runOpMode() throws InterruptedException {
        try {
            initOpMode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        waitForStart();
        while (opModeIsActive()) {
            if(gamepad1.right_bumper) {
                robot.control.toggleClaw(Control.ClawState.OPEN);
            }
            if(gamepad1.left_bumper) {
                robot.control.toggleClaw(Control.ClawState.CLOSED);
            }
            if(gamepad1.a) {
                robot.control.toggleClawAngle(Control.ClawAngleState.PICKUP);
            }
            if(gamepad1.b) {
                robot.control.toggleClawAngle(Control.ClawAngleState.DROPOFF);
            }
            if(gamepad1.x) {
                robot.control.toggleArm(Control.ArmState.PICKUP);
            }
            if(gamepad1.y) {
                robot.control.toggleArm(Control.ArmState.DROPOFF);
            }
//            if(gamepad1.a) {
//                robot.control.unfold();
//            }
//            if(gamepad1.b) {
//                robot.control.deploy();
//            }
        }
    }
}

