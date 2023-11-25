package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Subsystems.Vision.MarkerLocation;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.concurrent.TimeUnit;

@Autonomous(name = "Auto Red Right", group = "Auto")
public class AutoRedRight extends Auto {
    @SuppressWarnings("RedundantThrows")
    public void runOpMode() throws InterruptedException {
        initAuto(AllianceColor.RED);
        MarkerLocation markerPosition = robot.vision.detectMarkerRun();
        robot.vision.stop();
        waitForStart();
        controlThread.start();
        timer.reset();
        switch (markerPosition) {
            case LEFT:
                robot.drive.move(new Pose(0, 30 * mmPerInch, -90));
                controlThread.reachedPosition = true;
                robot.drive.moveVector(new Vector(0, 30 * mmPerInch));
                break;
            case MIDDLE:
                robot.drive.moveVector(new Vector(12 * mmPerInch, 0));
                controlThread.reachedPosition = true;
                robot.drive.move(new Pose(-42, 0, -90));
                break;
            case RIGHT:
                robot.drive.move(new Pose(12 * mmPerInch, 0, 90));
                controlThread.reachedPosition = true;
                robot.drive.move(new Pose(0, -60 * mmPerInch, -180));
                break;
        }

        adjustPosition(markerPosition);
        controlThread.reachedPosition = true;
        controlThread.extended.tryLock(100, TimeUnit.SECONDS);
        robot.drive.moveVector(new Vector(24, 0));
    }
}
