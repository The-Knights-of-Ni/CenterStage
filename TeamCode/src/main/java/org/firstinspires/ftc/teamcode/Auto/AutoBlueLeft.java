package org.firstinspires.ftc.teamcode.Auto;

import org.firstinspires.ftc.teamcode.Subsystems.Vision.MarkerDetectionPipeline;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Vector;

public class AutoBlueLeft extends Auto{

    public void runOpMode() {
        initAuto(AllianceColor.BLUE);
        robot.drive.moveVector(new Vector(24*mmPerInch, 0));
        MarkerDetectionPipeline.MarkerLocation markerPosition = robot.vision.detectMarkerRun();
        robot.vision.stop();
        waitForStart();
        timer.reset();
        robot.drive.moveVector(new Vector(20 * mmPerInch, 0));
        // Place pixel on tape
        robot.drive.moveAngle(-90);
        robot.drive.moveVector(new Vector(0, 14 * mmPerInch));
        //Detect apriltags
        //Place pixel on backdrop
        // Move out of the way
    }

}
