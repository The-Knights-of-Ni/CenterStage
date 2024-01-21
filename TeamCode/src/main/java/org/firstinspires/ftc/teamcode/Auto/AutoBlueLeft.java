package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Subsystems.Vision.MarkerDetectionPipeline;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.concurrent.TimeUnit;

@Autonomous(name = "Auto Blue Left", group = "Auto")
public class AutoBlueLeft extends Auto {
    @SuppressWarnings("RedundantThrows")
    public void runOpMode() throws InterruptedException {
        //robot begins to function
        initAuto(AllianceColor.BLUE);
        //MarkerDetectionPipeline.MarkerLocation markerPosition = robot.vision.detectMarkerRun();
        MarkerDetectionPipeline.MarkerLocation markerPosition = MarkerDetectionPipeline.MarkerLocation.MIDDLE; //Delete this line and uncomment the previous one once vision is working
        waitForStart();
        controlThread.start();
        timer.reset();
        switch (markerPosition) {
            case LEFT:
                // turns the robot left 90 degrees after moving the robot 30 in forward
                robot.drive.moveVector(new Vector(0, 30 * mmPerInch));
                robot.drive.moveAngle(90);
                // confirms position is reached
                controlThread.reachedPosition = true;
                // moving the robot 30 inches forward
                robot.drive.moveVector(new Vector(0, 30 * mmPerInch));
                break;
            case MIDDLE:
                // moving the robot 12 inches right
                robot.drive.moveVector(new Vector(0, 30* mmPerInch));
                // confirms position is reached
                controlThread.reachedPosition = true;
                // turn the robot left 90 degrees after moving it 42 inches left
                robot.drive.move(new Pose(-36 * mmPerInch, 0, 90));
                robot.drive.move(new Pose(-12 * mmPerInch, 0, 0));
                break;
            case RIGHT:
                //turns the robot right 90 degress after moving it 12 inches right
                robot.drive.move(new Pose(12 * mmPerInch, 0, 90));
                //confirms position is reached
                controlThread.reachedPosition = true;
                //turns the robot right 180 degrees after moving the robot 60 inches backward
                robot.drive.move(new Pose(0, -60 * mmPerInch, -180));
                break;
        }

//        adjustPosition(markerPosition);
        telemetry.addLine("passed switch statement");
        controlThread.start();
        controlThread.reachedPosition = true;
    }
}
