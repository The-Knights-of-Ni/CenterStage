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
                robot.drive.move(new Pose(12 * mmPerInch, 24 * mmPerInch, -90));
                controlThread.reachedPosition = true;
                robot.drive.moveVector(new Vector(0, 78 * mmPerInch));
                break;
            case MIDDLE:
                robot.drive.moveVector(new Vector(12 * mmPerInch, 0));
                controlThread.reachedPosition = true;
                robot.drive.move(new Pose(-112, 0, -90));
                break;
            case RIGHT:
                robot.drive.move(new Pose(12 * mmPerInch, 0, 90));
                controlThread.reachedPosition = true;
                robot.drive.move(new Pose(0, -100 * mmPerInch, -180));
                break;
        }

        adjustPosition(markerPosition);
        controlThread.reachedPosition = true;
        controlThread.extended.tryLock(100, TimeUnit.SECONDS);
        robot.drive.moveVector(new Vector(24, 0));
    }
}
