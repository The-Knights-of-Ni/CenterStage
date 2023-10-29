package org.firstinspires.ftc.teamcode.Testop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.teamcode.Auto.Auto;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Pose;

@Autonomous(name = "IMU Test", group = "Concept")
public class IMUDirectionalTest extends Auto {
    /**
     * Override of runOpMode()
     *
     * <p>Please do not swallow the InterruptedException, as it is used in cases where the op mode
     * needs to be terminated early.</p>
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
     */
    @Override
    @SuppressWarnings("RedundantThrows")
    public void runOpMode() throws InterruptedException {
        initAuto(AllianceColor.RED);
        waitForStart();
        timer.reset();
//        TODO: re-enable when new drive is enabled
//        robot.drive.imu.startAccelerationIntegration(new Position(DistanceUnit.MM, 0, 0, 0, 0),
//                new Velocity(DistanceUnit.MM, 0, 0, 0, 0), 100);
        while (opModeIsActive()) {
//            telemetry.addData("Heading", robot.drive.imu.getAngularOrientation().firstAngle);
//            telemetry.addData("Position", robot.drive.imu.getPosition());
            telemetry.update();
            Thread.sleep(10);
        }
    }
}
