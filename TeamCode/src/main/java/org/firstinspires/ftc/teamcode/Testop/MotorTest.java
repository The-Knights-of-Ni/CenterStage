package org.firstinspires.ftc.teamcode.Testop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Subsystems.Control.Control;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;

import java.util.HashMap;

@TeleOp(name = "Motor Position Test")
public class MotorTest extends LinearOpMode {
    private Robot robot;
    private double robotAngle;

    @Override
    public void runOpMode() {
        ElapsedTime timer = new ElapsedTime();
        HashMap<String, Boolean> flags = new HashMap<String, Boolean>();
        flags.put("vision", false);
        this.robot = new Robot(hardwareMap, telemetry, timer, AllianceColor.BLUE, gamepad1, gamepad2, flags);

        // Initializing start state
        robot.control.initDevicesTeleop();

        waitForStart();
        while (opModeIsActive()) {
            if(gamepad1.a) {
                robot.control.extendBar(Control.BarState.HIGH);
            }
            if(gamepad1.b) {
                robot.control.extendBar(Control.BarState.MIDDLE);
            }
            if(gamepad1.y) {
                robot.control.extendBar(Control.BarState.LOW);
            }
            if(gamepad1.x) {
                robot.control.extendBar(Control.BarState.PICKUP);
            }
            if(gamepad1.right_bumper) {
                robot.bar.setPower(1);
                robot.bar.setTargetPosition(robot.bar.getCurrentPosition() - 10);
                robot.bar.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            }
            if(gamepad1.left_bumper) {
                robot.bar.setPower(1);
                robot.bar.setTargetPosition(robot.bar.getCurrentPosition() + 10);
                robot.bar.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            }

            telemetry.addData("Position (bar)", robot.bar.getCurrentPosition());
            telemetry.update();
        }
    }


}
