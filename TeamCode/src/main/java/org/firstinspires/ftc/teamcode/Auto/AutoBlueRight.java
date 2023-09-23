package org.firstinspires.ftc.teamcode.Auto;

import org.firstinspires.ftc.teamcode.Subsystems.Control.Control;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Vector;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.*;

public class AutoBlueRight extends Auto{

    public void runOpMode() {
        initAuto(AllianceColor.BLUE);
        MarkerDetectionPipeline.MarkerLocation markerPosition = robot.vision.detectMarkerRun();
        robot.vision.stop();
        waitForStart();
        timer.reset();
        // Place pixel on tape
        switch (markerPosition) {
            case LEFT:
                robot.drive.moveVector(new Vector(12*mmPerInch, 24 * mmPerInch), -90);
                robot.control.moveLinearSlide(Control.SlidePosition.EXTENDED);
                robot.control.moveDoubleGrippy(Control.DoubleGrippyState.OPEN_FRONT);
                robot.control.moveLinearSlide(Control.SlidePosition.RETRACTED);
                robot.drive.moveVector(new Vector(0, 60 * mmPerInch));


                break;
            case MIDDLE:
                robot.drive.moveVector(new Vector(12 * mmPerInch, 0));
                robot.control.moveLinearSlide(Control.SlidePosition.EXTENDED);
                robot.control.moveDoubleGrippy(Control.DoubleGrippyState.OPEN_FRONT);
                robot.control.moveLinearSlide(Control.SlidePosition.RETRACTED);
                robot.drive.moveVector(new Vector(0,0),-90);
                robot.drive.moveVector(new Vector(0, 60 * mmPerInch));
                break;
            case RIGHT:
                robot.drive.moveVector(new Vector(12 * mmPerInch, 0),90);
                robot.control.moveLinearSlide(Control.SlidePosition.EXTENDED);
                robot.control.moveDoubleGrippy(Control.DoubleGrippyState.OPEN_FRONT);
                robot.control.moveLinearSlide(Control.SlidePosition.RETRACTED);
                robot.drive.moveVector(new Vector(0,0), -180);
                robot.drive.moveVector(new Vector(60,0));

                break;
        }
        robot.drive.moveAngle(-90);
        robot.drive.moveVector(new Vector(0, 56 * mmPerInch));
        //Detect apriltags
        //Place pixel on backdrop
        // Move out of the way
    }

}
