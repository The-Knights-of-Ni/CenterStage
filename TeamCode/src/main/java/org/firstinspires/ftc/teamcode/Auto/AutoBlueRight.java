package org.firstinspires.ftc.teamcode.Auto;

import org.firstinspires.ftc.teamcode.Subsystems.Vision.MarkerDetectionPipeline;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.concurrent.TimeUnit;

public class AutoBlueRight extends Auto {
    @SuppressWarnings("RedundantThrows")
    public void runOpMode() throws InterruptedException {
        initAuto(AllianceColor.BLUE);
        MarkerDetectionPipeline.MarkerLocation markerPosition = robot.vision.detectMarkerRun();
        robot.vision.stop();
        waitForStart();
        controlThread.start();
        timer.reset();
        switch (markerPosition) {
            case LEFT:
                //moves the robot 12 inches on the x-axis, 24 inches forward,
                // and turns left 90 degrees
                robot.drive.move(new Pose(12 * mmPerInch, 24 * mmPerInch, -90));
                //confirms position reached
                controlThread.reachedPosition = true;
                //moves the robot forward 78 inches inches
                robot.drive.moveVector(new Vector(0, 78 * mmPerInch));
                break;
            case MIDDLE:
                //moves the robot 12 inches on the x-axis
                robot.drive.moveVector(new Vector(12 * mmPerInch, 0));
                //confirms position reached
                controlThread.reachedPosition = true;
                //moves the robot -112 inches on the x-axis
                // and turn the robot 90 degrees left
                robot.drive.moveVector(new Vector(-112, 0), -90);
                break;
            case RIGHT:
                //moves the robot 12 inches on the x-axis
                //and turns the robot 90 degrees right
                robot.drive.moveVector(new Vector(12 * mmPerInch, 0), 90);
                //confirms position reached
                controlThread.reachedPosition = true;
                //moves the robot 100 inches backward
                // and turns the robot left 180 degrees
                robot.drive.moveVector(new Vector(0, -100 * mmPerInch), -180);
                break;
        }

        adjustPosition(markerPosition);
        controlThread.reachedPosition = true;
        controlThread.extended.tryLock(100, TimeUnit.SECONDS);
        robot.drive.moveVector(new Vector(-24, 0));
    }
}
