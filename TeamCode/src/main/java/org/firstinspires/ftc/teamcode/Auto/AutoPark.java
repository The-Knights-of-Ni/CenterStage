package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Subsystems.Vision.ConeColorPipeline.ConeColor;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Vector;

@Autonomous(name = "Auto Park")
public class AutoPark extends Auto{
    @Override
    public void runOpMode() throws InterruptedException {
        initAuto(AllianceColor.RED);
        waitForStart();
        timer.reset();
        robot.vision.start();
        ConeColor coneColor = ConeColor.OTHER;
        while(coneColor == ConeColor.OTHER) {
            coneColor = robot.vision.detectConeColor();
        }
        robot.vision.stop();
        telemetry.addData("Cone Color", coneColor.color);
        telemetry.update();
        switch (coneColor) {
            case GREEN:
                robot.drive.moveVector(new Vector(-24*mmPerInch, 0));
                robot.drive.moveVector(new Vector(0, 36*mmPerInch));
                break;
            case PINK:
                robot.drive.moveVector(new Vector(0, 36*mmPerInch));
                break;
            case ORANGE:
                robot.drive.moveVector(new Vector(24*mmPerInch, 0));
                robot.drive.moveVector(new Vector(0, 36*mmPerInch));
                break;
        }
    }
}
