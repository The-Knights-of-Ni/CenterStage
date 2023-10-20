package org.firstinspires.ftc.teamcode.Auto;

import org.firstinspires.ftc.teamcode.Subsystems.Vision.MarkerDetectionPipeline;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.concurrent.TimeUnit;

public class AutoBlueLeft extends Auto {
    @SuppressWarnings("RedundantThrows")
    public void runOpMode() throws InterruptedException {
        //robot begins to function
        initAuto(AllianceColor.BLUE);
        MarkerDetectionPipeline.MarkerLocation markerPosition = robot.vision.detectMarkerRun();
        robot.vision.stop();
        waitForStart();
        controlThread.start();
        timer.reset();
        switch (markerPosition) {
            case LEFT:
                //turns the robot left 90 degrees after moving the robot 30 in forward
                robot.drive.move(new Pose(0, 30 * mmPerInch, -90));
                //confirms position is reached
                controlThread.reachedPosition = true;
                //moving the robot 30 inches forward
                robot.drive.moveVector(new Vector(0, 30 * mmPerInch));
                break;
            case MIDDLE:
                //moving the robot 12 inches on the x-axis
                robot.drive.moveVector(new Vector(12 * mmPerInch, 0));
                //confirms position is reached
                controlThread.reachedPosition = true;
                //turn the robot left 90 degrees after moving it -42 inches on the x-axis
                robot.drive.move(new Pose(-42, 0, -90));
                break;
            case RIGHT:
                //turns the robot right 90 degress after moving it 12 inches on the x-axis
                robot.drive.move(new Pose(12 * mmPerInch, 0, 90));
                //confirms position is reached
                controlThread.reachedPosition = true;
                //turns the robot right 180 degrees after moving the robot -60 inches backward
                robot.drive.move(new Pose(0, -60 * mmPerInch, -180));
                break;
        }

        adjustPosition(markerPosition);
        controlThread.reachedPosition = true;
        controlThread.extended.tryLock(100, TimeUnit.SECONDS);
        robot.drive.moveVector(new Vector(-24, 0));
    }
}
