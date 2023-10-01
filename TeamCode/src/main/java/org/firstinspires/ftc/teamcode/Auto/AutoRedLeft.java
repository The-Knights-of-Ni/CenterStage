package org.firstinspires.ftc.teamcode.Auto;

import org.firstinspires.ftc.teamcode.Subsystems.Vision.MarkerDetectionPipeline;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Vector;

public class AutoRedLeft extends Auto{
    public void runOpMode() {
        initAuto(AllianceColor.RED);
        MarkerDetectionPipeline.MarkerLocation markerPosition = robot.vision.detectMarkerRun();
        robot.vision.stop();
        waitForStart();
        thread.start();
        timer.reset();
        switch (markerPosition) {
            case LEFT:
                robot.drive.moveVector(new Vector(12*mmPerInch, 24 * mmPerInch), -90);
                thread.reachedPosition = true;
                robot.drive.moveVector(new Vector(0,78*mmPerInch));
                break;
            case MIDDLE:
                robot.drive.moveVector(new Vector(12 * mmPerInch, 0));
                thread.reachedPosition = true;
                robot.drive.moveVector(new Vector(-112, 0), -90);
                break;
            case RIGHT:
                robot.drive.moveVector(new Vector(12 * mmPerInch, 0),90);
                thread.reachedPosition = true;
                robot.drive.moveVector(new Vector(0, -100 * mmPerInch), -180);
                break;
        }

        adjustPosition(markerPosition);
        thread.reachedPosition = true;
        while(!thread.retracted) {}
        robot.drive.moveVector(new Vector(24,0));
    }
}
