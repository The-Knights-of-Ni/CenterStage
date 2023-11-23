package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Vector;

@Autonomous(name = "Auto Red Right Park", group = "Auto")
public class AutoRedRightPark extends Auto {
    @SuppressWarnings("RedundantThrows")
    public void runOpMode() throws InterruptedException {
        initAuto(AllianceColor.RED);
        //MarkerDetectionPipeline.MarkerLocation markerPosition = robot.vision.detectMarkerRun();
        //robot.vision.stop();
        waitForStart();
        timer.reset();
        robot.control.closeClaw();
        robot.drive.moveVector(new Vector(0, 4 * mmPerInch));
        robot.drive.moveVector(new Vector(52 * mmPerInch, 0));
    }
}
