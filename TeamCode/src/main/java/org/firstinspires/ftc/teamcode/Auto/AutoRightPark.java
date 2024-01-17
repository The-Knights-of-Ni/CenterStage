package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Vector;

@Autonomous(name = "Auto Right Park", group = "Auto")
public class AutoRightPark extends Auto {
    @SuppressWarnings("RedundantThrows")
    public void runOpMode() throws InterruptedException {
        initAuto(AllianceColor.RED);
        robot.control.extendShoulder();
        robot.control.openClaw();
        //MarkerDetectionPipeline.MarkerLocation markerPosition = robot.vision.detectMarkerRun();
        //robot.vision.stop();
        waitForStart();
        timer.reset();
        robot.drive.moveVector(new Vector(0, 4 * mmPerInch));
        robot.drive.moveVector(new Vector(95 * mmPerInch, 0));
    }
}
