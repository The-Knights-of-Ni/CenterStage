package org.firstinspires.ftc.teamcode.Testop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Auto.Auto;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;

@Autonomous(name = "Crane Calibration", group = "Concept")
public class CraneCalibration extends Auto {
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
        initAuto(AllianceColor.BLUE);
        waitForStart();
        timer.reset();
        while (opModeIsActive()) {
            telemetry.addData("Motor Encoder", "Crane Position: %d", robot.control.slideMotor.getCurrentPosition());
            telemetry.update();
        }
    }
}
