package org.firstinspires.ftc.teamcode.Auto;

import android.util.Log;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Drive;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.MarkerDetectionPipeline;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.HashMap;

/**
 * Auto creates a robot and runs it in auto mode.
 *
 * @see LinearOpMode
 */
public abstract class Auto extends LinearOpMode {

    /**
     * Number of millimeters per an inch
     */
    public static final float mmPerInch = (float) Drive.mmPerInch;
    /**
     * The robot class in the op mode
     */
    public Robot robot;
    public ElapsedTime timer;

    public ArmMovementThread controlThread;

    /**
     * Initializes the robot class and sets the robot as the newly initialized robot.
     *
     * @param allianceColor The alliance color
     */
    public void initAuto(AllianceColor allianceColor) {
        Log.i("main", "*** Opmode control passed to teamcode ***");
        timer = new ElapsedTime();
        HashMap<String, Boolean> flags = new HashMap<>();
        flags.put("vision", true);
        flags.put("web", true);
        this.robot = new Robot(hardwareMap, telemetry, timer, allianceColor, gamepad1, gamepad2, flags);
        robot.control.initDevicesAuto();
        controlThread = new ArmMovementThread(robot.control);
        robot.control.closeClaw();
        telemetry.addData("Waiting for start", "");
        telemetry.update();
    }

    public void adjustPosition(MarkerDetectionPipeline.MarkerLocation location) {
        switch (location) {
            case LEFT:
                robot.drive.moveVector(new Vector(-9 * mmPerInch, 0));
                break;

            case MIDDLE:
                break;

            case RIGHT:
                robot.drive.moveVector(new Vector(9 * mmPerInch, 0));
                break;
        }
    }
}
