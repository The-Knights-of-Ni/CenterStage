package org.firstinspires.ftc.teamcode.Auto;

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
        //MarkerDetectionPipeline.MarkerLocation markerPosition = robot.vision.detectMarkerRun();
        MarkerDetectionPipeline.MarkerLocation markerPosition = MarkerDetectionPipeline.MarkerLocation.NOT_FOUND; //Delete this line and uncomment the previous one once vision is working
        waitForStart();
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
                robot.drive.moveVector(new Vector(0, 30 * mmPerInch));
                // confirms position is reached
                // turn the robot left 90 degrees after moving it 42 inches left
                robot.drive.move(new Pose(-36 * mmPerInch, 0, 0));
                robot.drive.moveAngle(-90);
                robot.drive.move(new Pose(-12 * mmPerInch, 0, 0));
                break;
            case RIGHT:
                //turns the robot right 90 degrees after moving it 12 inches right
                robot.drive.move(new Pose(12 * mmPerInch, 0, 0));
                robot.drive.moveAngle(90);
                //confirms position is reached
                //turns the robot right 180 degrees after moving the robot 60 inches backward
                robot.drive.move(new Pose(0, -60 * mmPerInch, 0));
                robot.drive.moveAngle(180);
                break;
            default:
                break;
        }
        telemetry.addLine("passed switch statement");
        robot.control.openClawSync();
        telemetry.addData("RunMode: ", robot.control.slideMotor.getMode());
        telemetry.addData("Current Position: ", robot.control.slideMotor.getCurrentPosition());
        telemetry.addData("Target Position: ", robot.control.slideMotor.getTargetPosition());
        telemetry.addData("Is Busy: ", robot.control.slideMotor.isBusy());
        telemetry.update();
//        robot.control.moveLinearSlideSync(Control.SlidePosition.UP);
        robot.control.extendShoulder();
    }
}
