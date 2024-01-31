package org.firstinspires.ftc.teamcode.Auto;

import android.util.Log;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Subsystems.Control.Control;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.MarkerDetectionPipeline;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Vector;


@Autonomous(name = "Auto Blue Left", group = "Auto")
public class AutoBlueLeft extends Auto {
    @SuppressWarnings("RedundantThrows")
    public void runOpMode() throws InterruptedException {
        //robot begins to function
        initAuto(AllianceColor.BLUE);
        waitForStart();
        MarkerDetectionPipeline.MarkerLocation markerPosition = robot.vision.detectMarkerRun(); //Delete this line and uncomment the previous one once vision is working
        Log.d("Marker Location", String.valueOf(markerPosition));
        timer.reset();
        switch (markerPosition) {
            case LEFT:
                // turns the robot left 90 degrees after moving the robot 30 in forward
                robot.drive.moveVector(new Vector(0, 30 * mmPerInch));
                robot.drive.moveAngle(90);
                // confirms position is reached
                // moving the robot 30 inches forward
                robot.drive.moveVector(new Vector(0, 30 * mmPerInch));
                break;
            case MIDDLE:
                // moving the robot 12 inches right
                robot.drive.moveVector(new Vector(-8, 29 * mmPerInch));
                robot.drive.moveVector(new Vector(0, -2 * mmPerInch));
                // confirms position is reached
                // turn the robot left 90 degrees after moving it 42 inches left
                robot.drive.moveVector(new Vector(-30 * mmPerInch, 0));
                robot.drive.moveAngle(90);
                break;
            case RIGHT:
                //turns the robot right 90 degrees after moving it 12 inches right
                robot.drive.moveVector(new Vector(12 * mmPerInch, 0));
                robot.drive.moveAngle(90);
                //confirms position is reached
                //turns the robot right 180 degrees after moving the robot 60 inches backward
                robot.drive.moveVector(new Vector(0, -60 * mmPerInch));
                robot.drive.moveAngle(180);
                break;
            default:
                break;
        }
        telemetry.addLine("passed switch statement");
        telemetry.update();
        robot.control.openClawSync();
        robot.control.moveLinearSlide(Control.SlidePosition.UP);
//        robot.control.extendShoulder();
    }
}
