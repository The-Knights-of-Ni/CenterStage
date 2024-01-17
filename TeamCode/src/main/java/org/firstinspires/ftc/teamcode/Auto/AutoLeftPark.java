package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Vector;

@Autonomous(name = "Auto Left Park", group = "Auto")
public class AutoLeftPark extends Auto {
    @SuppressWarnings("RedundantThrows")
    public void runOpMode() throws InterruptedException {
        //robot begins to function
        initAuto(AllianceColor.BLUE);
        robot.control.extendShoulder();
        robot.control.openClaw();
        //MarkerDetectionPipeline.MarkerLocation markerPosition = robot.vision.detectMarkerRun();
        //robot.vision.stop();
        waitForStart();
        timer.reset();
        robot.drive.moveVector(new Vector(0, 4 * mmPerInch));
        robot.drive.moveVector(new Vector(-95 * mmPerInch, 0));
    }
}
