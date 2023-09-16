package org.firstinspires.ftc.teamcode.Auto;

import org.firstinspires.ftc.teamcode.Util.Vector;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.*;

public class BlueAllienceBackdrop extends Auto{

    public void runOpMode()
    {
        robot.drive.moveVector(new Vector(24*mmPerInch, 0));
        MarkerDetectionPipeline.MarkerLocation markerPosition = robot.vision.detectMarkerRun();
        if (markerPosition.equals(MarkerDetectionPipeline.MarkerLocation.RIGHT))
        {
            //Place pixel right
        }
        else if (markerPosition.equals(MarkerDetectionPipeline.MarkerLocation.MIDDLE))
        {
            //Place pixel center
        }
        else if (markerPosition.equals(MarkerDetectionPipeline.MarkerLocation.LEFT))
        {
            //Place pixel left
        }

        robot.drive.moveVector(new Vector(-24*mmPerInch, 0));
        robot.drive.moveVector(new Vector(0,24*mmPerInch));
        robot.drive.moveAngle(-90);
        //Detect apriltags
        //Place pixel on backdrop
    }

}
