package org.firstinspires.ftc.teamcode.Testop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.ConeColorPipeline;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;

import java.util.HashMap;

/**
 * This shows what the camera is seeing
 */
@Autonomous(name = "Vision Test", group = "Concept")
public class VisionTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry, new ElapsedTime(), AllianceColor.BLUE, gamepad1, gamepad2, new HashMap<>());

        waitForStart();
        robot.vision.start();
        ConeColorPipeline.ConeColor coneColor = ConeColorPipeline.ConeColor.OTHER;
        while(coneColor == ConeColorPipeline.ConeColor.OTHER) {
            coneColor = robot.vision.detectConeColor();
        }
        robot.vision.stop();
        telemetry.addData("cone color", coneColor.color);
        telemetry.update();
        sleep(10000);
    }
}
