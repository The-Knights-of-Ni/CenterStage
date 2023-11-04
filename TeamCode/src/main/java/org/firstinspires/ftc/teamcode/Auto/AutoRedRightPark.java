package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Subsystems.Vision.MarkerDetectionPipeline;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.concurrent.TimeUnit;

@Autonomous(name = "Auto Red Right Park", group = "Auto")
public class AutoRedRightPark extends Auto {
    @SuppressWarnings("RedundantThrows")
    public void runOpMode() throws InterruptedException {
        initAuto(AllianceColor.RED);
        //MarkerDetectionPipeline.MarkerLocation markerPosition = robot.vision.detectMarkerRun();
        //robot.vision.stop();
        waitForStart();
        timer.reset();
        robot.drive.moveVector(new Vector(52, 0));
    }
}