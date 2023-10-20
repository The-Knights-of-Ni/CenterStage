package org.firstinspires.ftc.teamcode.Auto;

import org.firstinspires.ftc.teamcode.Subsystems.Vision.MarkerDetectionPipeline;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.concurrent.TimeUnit;

public class AutoRedRight extends Auto {
    @SuppressWarnings("RedundantThrows")
    public void runOpMode() throws InterruptedException {
        initAuto(AllianceColor.RED);
        MarkerDetectionPipeline.MarkerLocation markerPosition = robot.vision.detectMarkerRun();
        robot.vision.stop();
        waitForStart();
        controlThread.start();
        timer.reset();
        switch (markerPosition) {
            case LEFT:
                //30 inches forward, 90 degrees left
                robot.drive.move(new Pose(0, 30 * mmPerInch, -90));
                controlThread.reachedPosition = true;
                //30 inches forward
                robot.drive.moveVector(new Vector(0, 30 * mmPerInch));
                break;
            case MIDDLE:
                //12 inches right
                robot.drive.moveVector(new Vector(12 * mmPerInch, 0));
                controlThread.reachedPosition = true;
                //42 inches left, 90 degrees left
                robot.drive.move(new Pose(-42*mmPerInch, 0, -90));
                break;
            case RIGHT:
                //12 inches right, 90 degrees right
                robot.drive.move(new Pose(12 * mmPerInch, 0, 90));
                controlThread.reachedPosition = true;
                //60 inches backward, turns robot around
                robot.drive.move(new Pose(0, -60 * mmPerInch, -180));
                break;
        }

        adjustPosition(markerPosition);
        controlThread.reachedPosition = true;
        controlThread.extended.tryLock(100, TimeUnit.SECONDS);
        //Moves robot 24 inches right
        robot.drive.moveVector(new Vector(24, 0));
    }
}
