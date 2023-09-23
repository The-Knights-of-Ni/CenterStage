package org.firstinspires.ftc.teamcode.Subsystems.Control;

import org.firstinspires.ftc.teamcode.Subsystems.Control.Control.*;
import org.firstinspires.ftc.teamcode.Robot;

public class ScorePixelThread extends Thread {
    private Robot robot;
    public ScorePixelThread(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void run() {
        robot.control.intakePixel();
        robot.control.moveDoubleGrippy(DoubleGrippyState.CLOSE_SIMUL);
        robot.control.moveLinearSlide(SlidePosition.EXTENDED);
        robot.control.pivotDoubleGrippy(0); //TODO: Calibrate this constant for extension toward the board
        robot.control.moveDoubleGrippy(DoubleGrippyState.OPEN_SIMUL);
        robot.control.pivotDoubleGrippy(0); //TODO: Calibrate this constant for retraction.
        robot.control.moveLinearSlide(SlidePosition.OBJ_PICKUP);
    }

}
