package org.firstinspires.ftc.teamcode.Auto;

import org.firstinspires.ftc.teamcode.Subsystems.Vision.MarkerDetectionPipeline;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Vector;

public class AutoRedRight extends Auto {
    public void runOpMode() throws InterruptedException {
        initAuto(AllianceColor.RED);
        MarkerDetectionPipeline.MarkerLocation markerPosition = robot.vision.detectMarkerRun();
        robot.vision.stop();
        waitForStart();
        controlThread.start();
        timer.reset();
        switch (markerPosition) {
            case LEFT:
                robot.drive.moveVector(new Vector(0, 30 * mmPerInch), -90);
                controlThread.reachedPosition = true;
                robot.drive.moveVector(new Vector(0,30 * mmPerInch));
                break;
            case MIDDLE:
                robot.drive.moveVector(new Vector(12 * mmPerInch, 0));
                controlThread.reachedPosition = true;
                robot.drive.moveVector(new Vector(-42, 0), -90);
                break;
            case RIGHT:
                robot.drive.moveVector(new Vector(12 * mmPerInch, 0),90);
                controlThread.reachedPosition = true;
                robot.drive.moveVector(new Vector(0, -60 * mmPerInch), -180);
                break;
        }

        adjustPosition(markerPosition);
        controlThread.reachedPosition = true;
        while (!controlThread.retracted) {}
        robot.drive.moveVector(new Vector(24, 0));
    }
}
