package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Subsystems.Control.Control.*;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;

import java.io.IOException;
import java.util.HashMap;

@TeleOp(name = "TeleOp")
public class Teleop extends LinearOpMode {
    double deltaT;
    double timeCurrent;
    double timePre;
    ElapsedTime timer;
    private Robot robot;

    private boolean driveHighPower = true;

    private void initOpMode() throws IOException {
        // Initialize DC motor objects
        timer = new ElapsedTime();
        HashMap<String, Boolean> flags = new HashMap<>();
        flags.put("vision", false);
        this.robot =  new Robot(hardwareMap, telemetry, timer, AllianceColor.BLUE, gamepad1, gamepad2,flags);
        timeCurrent = timer.nanoseconds();
        timePre = timeCurrent;

        telemetry.addData("Waiting for start", "...");
        telemetry.update();
    }

    /**
     * Override of runOpMode()
     *
     * <p>Please do not swallow the InterruptedException, as it is used in cases where the op mode
     * needs to be terminated early.
     *
     * @see LinearOpMode
     */
    @Override
    public void runOpMode() throws InterruptedException {
        try {
            initOpMode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ElapsedTime timer = new ElapsedTime();
        robot.control.initDevicesTeleop();
        waitForStart();

        telemetry.clearAll();
        timeCurrent = timer.nanoseconds();
        timePre = timeCurrent;

        final double sensitivityHighPower = 1.0; // multiply inputs with this on high power mode
        final double sensitivityLowPower = 0.7; // multiply inputs with this on non-high power mode

        while (opModeIsActive()) {
            robot.updateGamepads();

            timeCurrent = timer.nanoseconds();
            deltaT = timeCurrent - timePre;
            timePre = timeCurrent;

            double[] motorPowers;
            if (robot.gamepad1.yButton.isPressed()) { //Turbo button!
                motorPowers = robot.drive.calcMotorPowers(robot.gamepad1.leftStickX * sensitivityHighPower, robot.gamepad1.leftStickY * sensitivityHighPower, robot.gamepad1.rightStickX * sensitivityHighPower);
            }
            else {
                motorPowers = robot.drive.calcMotorPowers(robot.gamepad1.leftStickX * sensitivityLowPower, robot.gamepad1.leftStickY * sensitivityLowPower, robot.gamepad1.rightStickX * sensitivityLowPower);
            }

            robot.drive.setDrivePowers(motorPowers);

            if(robot.gamepad1.xButton.toggle) {
                robot.control.craneLift(CraneState.DOWN);
            }

            if(robot.gamepad1.bButton.toggle) {
                robot.control.airplaneLaunch(PlaneLaunchRange.MEDIUM);
            }

            if(robot.gamepad2.bButton.toggle) {
                robot.control.intakePixel();
            }

            if(robot.gamepad2.aButton.toggle) {
                robot.control.moveLinearSlide(SlidePosition.EXTENDED);
                robot.control.moveDoubleGrippy(DoubleGrippyState.OPEN_SIMUL);
            }

            Thread.sleep(10); // Ten milli sleep so that the CPU doesn't die (this also means 10 ms baseline lag)
        }
    }
}
