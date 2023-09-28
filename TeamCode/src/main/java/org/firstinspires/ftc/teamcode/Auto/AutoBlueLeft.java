package org.firstinspires.ftc.teamcode.Auto;

import org.firstinspires.ftc.teamcode.Subsystems.Control.Control;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.MarkerDetectionPipeline;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Vector;

public class AutoBlueLeft extends Auto{

    public void placePixel() {
        robot.control.moveLinearSlideSync(Control.SCORE_LOW_SLIDE);
        robot.control.openClawSync();
        robot.control.moveLinearSlideSync(Control.RETRACTED_SLIDE);
        robot.control.closeClawSync();
        robot.control.openClaw();
    }

    public void runOpMode() {
        initAuto(AllianceColor.BLUE);
        MarkerDetectionPipeline.MarkerLocation markerPosition = robot.vision.detectMarkerRun();
        robot.vision.stop();
        waitForStart();
        timer.reset();
        switch (markerPosition) {
            case LEFT:
                robot.drive.moveVector(new Vector(12*mmPerInch, 24 * mmPerInch), -90);
                placePixel();
                robot.drive.moveVector(new Vector(0,30*mmPerInch));
                break;
            case MIDDLE:
                robot.drive.moveVector(new Vector(12 * mmPerInch, 0));
                placePixel();
                robot.drive.moveVector(new Vector(-42, 0), -90);
                break;
            case RIGHT:
                robot.drive.moveVector(new Vector(12 * mmPerInch, 0),90);
                placePixel();
                robot.drive.moveVector(new Vector(0, -60 * mmPerInch), -180);
                break;
        }
        // TODO: Correct position first
        robot.control.closeClawSync();
        robot.control.moveLinearSlideSync(Control.SCORE_LOW_SLIDE);
        robot.control.moveLinearSlideSync(Control.RETRACTED_SLIDE);
        robot.drive.moveVector(new Vector(-24,0));
    }

}
