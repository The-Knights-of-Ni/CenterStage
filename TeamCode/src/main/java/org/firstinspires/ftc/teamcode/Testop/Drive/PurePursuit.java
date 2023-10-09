package org.firstinspires.ftc.teamcode.Testop.Drive;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Auto.Auto;
import org.firstinspires.ftc.teamcode.Geometry.Path;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.ArrayList;
import java.util.Arrays;

@Autonomous(name = "Pure Pursuit Test", group = "Concept")
public class PurePursuit extends Auto {
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
        ArrayList<Pose> poses = new ArrayList<>(
                Arrays.asList(new Pose(10 * mmPerInch, 10 * mmPerInch, 0),
                        new Pose(25 * mmPerInch, 0, 0)));
        Path path = new Path(poses);
        robot.drive.purePursuit(path);
        sleep(2000);
    }
}
