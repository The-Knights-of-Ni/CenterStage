package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Subsystems.Control.Control;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;

import java.io.IOException;
import java.util.HashMap;

@TeleOp(name = "Teleop")
public class Teleop extends LinearOpMode {
    double deltaT;
    double timeCurrent;
    double timePre;
    ElapsedTime timer;
    private Robot robot;

    private boolean driveHighPower = true;
    private boolean armDeployed = false;

    private void initOpMode() throws IOException {
        // Initialize DC motor objects
        timer = new ElapsedTime();
        HashMap<String, Boolean> flags = new HashMap<String, Boolean>();
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

        while (opModeIsActive()) { // clearer nomenclature for variables
            robot.getGamePadInputs();

            timeCurrent = timer.nanoseconds();
            deltaT = timeCurrent - timePre;
            timePre = timeCurrent;

            if(robot.yButton) {
                driveHighPower = true;
            } else {
                driveHighPower = false;
            }

            if(robot.bButton) {
                robot.bar.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }

            if(robot.aButton && !robot.isaButtonPressedPrev) {
                if(armDeployed) {
                    robot.control.pickupArm();
                    armDeployed = false;
                } else {
                    robot.control.dropoffArm();
                    armDeployed = true;
                }
            }

            // Robot drive movement
            double[] motorPowers;
            if (driveHighPower) {
                motorPowers = robot.drive.calcMotorPowers(robot.leftStickX * sensitivityHighPower, robot.leftStickY * sensitivityHighPower, robot.rightStickX * sensitivityHighPower);
            }
            else {
                motorPowers = robot.drive.calcMotorPowers(robot.leftStickX * sensitivityLowPower, robot.leftStickY * sensitivityLowPower, robot.rightStickX * sensitivityLowPower);
            }
            robot.drive.setDrivePowers(motorPowers);

            // Score state control
            if(robot.dPadUp || robot.dPadUp2) {
                robot.control.deploy(Control.BarState.HIGH);
            }
            if(robot.dPadLeft || robot.dPadLeft2) {
                robot.control.deploy(Control.BarState.LOW);
            }
            if(robot.dPadRight || robot.dPadRight2) {
                robot.control.deploy(Control.BarState.MIDDLE);
            }
            if(robot.dPadDown || robot.dPadDown2) {
                robot.control.retract();
            }

            // Manual 4-bar override
            if(robot.bumperRight || robot.bumperRight2) {
                robot.bar.setPower(1);
                robot.bar.setTargetPosition(robot.bar.getCurrentPosition() + 150);
                robot.bar.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            }
            if(robot.bumperLeft || robot.bumperLeft2) {
                robot.bar.setPower(1);
                robot.bar.setTargetPosition(robot.bar.getCurrentPosition() - 150);
                robot.bar.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            }

            // Claw open-close
            if(robot.triggerRight > 0.5) {
                robot.control.toggleClaw(Control.ClawState.OPEN);
            }
            if(robot.triggerLeft > 0.5) {
                robot.control.toggleClaw(Control.ClawState.CLOSED);
            }

        }
    }
}
