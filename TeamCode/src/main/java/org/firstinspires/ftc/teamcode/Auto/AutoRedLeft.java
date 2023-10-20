package org.firstinspires.ftc.teamcode.Auto;

import org.firstinspires.ftc.teamcode.Subsystems.Vision.MarkerDetectionPipeline;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.concurrent.TimeUnit;

public class AutoRedLeft extends Auto {
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
                //moves the robot 12 inches right, 24 inches forward, 90 degrees left
                robot.drive.move(new Pose(12 * mmPerInch, 24 * mmPerInch, -90));
                //reached position
                controlThread.reachedPosition = true;
                //moves the robot 78 inches forward
                robot.drive.moveVector(new Vector(0, 78 * mmPerInch));
                break;
            case MIDDLE:
                //12 inches right movement
                robot.drive.moveVector(new Vector(12 * mmPerInch, 0));
                //reached position
                controlThread.reachedPosition = true;
                //moves 112 movement backward, 90 degrees left
                robot.drive.move(new Pose(-112 * mmPerInch, 0, -90));
                break;
            case RIGHT:
                //12 inches right movement, 90 degrees right
                robot.drive.move(new Pose(12 * mmPerInch, 0, 90));
                //reached position
                controlThread.reachedPosition = true;
                //100 inches backward, turns robot around
                robot.drive.move(new Pose(0, -100 * mmPerInch, -180));
                break;
        }

        adjustPosition(markerPosition);
        controlThread.reachedPosition = true;
        controlThread.extended.tryLock(100, TimeUnit.SECONDS);
        //moves 24 inches right
        robot.drive.moveVector(new Vector(24, 0));
    }
}
