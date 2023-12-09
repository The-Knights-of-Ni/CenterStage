package org.firstinspires.ftc.teamcode.Testop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Auto.Auto;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;

@Autonomous(name = "Vision Test", group = "Concept")
public class VisionTest extends Auto {
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

        while (opModeIsActive()) {
            Thread.sleep(10);
        }
    }
}
