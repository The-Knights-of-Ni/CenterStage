package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Subsystems.Vision.MarkerDetectionPipeline;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.concurrent.TimeUnit;

@Autonomous(name = "Auto Blue Left Park", group = "Auto")
public class AutoBlueLeftPark extends Auto {
    @SuppressWarnings("RedundantThrows")
    public void runOpMode() throws InterruptedException {
        //robot begins to function
        initAuto(AllianceColor.BLUE);
        //MarkerDetectionPipeline.MarkerLocation markerPosition = robot.vision.detectMarkerRun();
        //robot.vision.stop();
        waitForStart();
        timer.reset();
        robot.control.closeClaw();
        robot.drive.moveVector(new Vector(0, 4 * mmPerInch));
        robot.drive.moveVector(new Vector(-52 * mmPerInch, 0));
    }
}
