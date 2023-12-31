package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.GamepadWrapper;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Subsystems.Control.ScorePixelThread;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotorGeneric;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;

import java.util.HashMap;

import static java.lang.Math.abs;

@TeleOp(name = "TeleOp")
public class Teleop extends LinearOpMode {
    double deltaT;
    double timeCurrent;
    double timePre;
    double airplaneAngle;
    ElapsedTime timer;
    private Robot robot;

    private void initOpMode() {
        // Initialize DC motor objects
        timer = new ElapsedTime();
        HashMap<String, Boolean> flags = new HashMap<>();
        flags.put("web", false);
        flags.put("vision", false);
        this.robot = new Robot(hardwareMap, telemetry, timer, AllianceColor.BLUE, gamepad1, gamepad2, flags);
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
        initOpMode();

        ElapsedTime timer = new ElapsedTime();
        robot.control.initDevices();
        waitForStart();

        telemetry.clearAll();
        timeCurrent = timer.nanoseconds();
        timePre = timeCurrent;

        final double sensitivityHighPower = 1.0; // multiply inputs with this on high power mode
        final double sensitivityLowPower = 0.5; // multiply inputs with this on non-high power mode
        boolean twoGamepads = true;
        double slidePowerVel = 0.0;
        double cranePowerVel = 0.0;

        while (opModeIsActive()) {
            robot.updateGamepads();

            timeCurrent = timer.nanoseconds();
            deltaT = timeCurrent - timePre;
            timePre = timeCurrent;
            if (twoGamepads) {
                MotorGeneric<Double> motorPowers;
                if (!robot.gamepad1.yButton.toggle) {
                    motorPowers = robot.drive.calcMotorPowers(sensitivityHighPower * robot.gamepad1.leftStickX, sensitivityHighPower * robot.gamepad1.leftStickY, sensitivityHighPower * robot.gamepad1.rightStickX);
                } else {
                    motorPowers = robot.drive.calcMotorPowers(sensitivityLowPower * robot.gamepad1.leftStickX, sensitivityLowPower * robot.gamepad1.leftStickY, sensitivityLowPower * robot.gamepad1.rightStickX);
                }

                robot.drive.setDrivePowers(motorPowers);

                // Close claw and score ...
                // TODO: Fix this
                if (robot.gamepad1.aButton.isPressed()) {
                    new ScorePixelThread(robot.control).start();
                }

                // Paper Drone

                if(robot.gamepad2.dPadLeft.isPressed()) {
                    robot.control.airplaneLaunch();
                }

                if(robot.gamepad2.bumperRight.isPressed()) {
                    robot.control.setAirplaneAngle();
                }

                if(robot.gamepad2.bumperLeft.isPressed()) {
                    robot.control.resetAirplaneAngle();
                }

                if(robot.gamepad1.bumperRight.isPressed()) {
                    robot.control.runIntake();
                }

                if(robot.gamepad1.bumperLeft.isPressed()) {
                    robot.control.stopIntake();
                }

                // Claw
                if (robot.gamepad2.aButton.isPressed()) {
                    robot.control.openClaw();
                }
                if (robot.gamepad2.bButton.isPressed()) {
                    robot.control.closeClaw();
                }
                if(robot.gamepad2.dPadUp.isPressed()) {
                    robot.control.extendShoulder();
                }
                if(robot.gamepad2.dPadRight.isPressed()) {
                    robot.control.retractShoulder();
                }
                if(robot.gamepad2.dPadDown.isPressed()) {
                    robot.control.pickupPosShoulder();
                }

                // Linear Slide
                // TODO: Implement auto correcting of claw shoulder
                slidePowerVel = robot.gamepad2.triggerLeft - robot.gamepad2.triggerRight;
                if (abs(slidePowerVel) >= 0.15) {
                    robot.control.setLinearSlideMotorPower(slidePowerVel);
                } else {
                    robot.control.setLinearSlideMotorPower(0);
                    robot.control.setCraneMotorPower(0);
                }
                cranePowerVel = robot.gamepad1.triggerLeft - robot.gamepad1.triggerRight;
                if (abs(cranePowerVel) >= 0.15) {
                    robot.control.setCraneMotorPower(cranePowerVel);
                }

                // April Tag Correction
                /*if (Robot.gamepad2.dPadLeft.isPressed()) {
                    robot.vision.aprilTagDetectionThread.currentDetections.stream().filter(tagDetection -> tagDetection.id == 1 || tagDetection.id == 4).findFirst().ifPresent(
                            aprilTagDetection -> robot.drive.moveVector(new Vector(aprilTagDetection.ftcPose.x * Drive.mmPerInch, 0))
                    );
                } else if (Robot.gamepad2.dPadUp.isPressed()) {
                    robot.vision.aprilTagDetectionThread.currentDetections.stream().filter(tagDetection -> tagDetection.id == 2 || tagDetection.id == 5).findFirst().ifPresent(
                            aprilTagDetection -> robot.drive.moveVector(new Vector(aprilTagDetection.ftcPose.x * Drive.mmPerInch, 0))
                    );
                } else if (Robot.gamepad2.dPadRight.isPressed()) {
                    robot.vision.aprilTagDetectionThread.currentDetections.stream().filter(tagDetection -> tagDetection.id == 3 || tagDetection.id == 6).findFirst().ifPresent(
                            aprilTagDetection -> robot.drive.moveVector(new Vector(aprilTagDetection.ftcPose.x * Drive.mmPerInch, 0))
                    );
                }*/

                // Switch to one gamepad
                if ((robot.gamepad1.bButton.isPressed() && robot.gamepad1.xButton.isPressed()) || (robot.gamepad2.bButton.isPressed() && robot.gamepad2.xButton.isPressed())) {
                    twoGamepads = false;
                }

            } else {
                // Must use gamepad 1 for one gamepad TODO: Find elegant fix
                MotorGeneric<Double> motorPowers;
                double triggerHit = GamepadWrapper.joystickDeadzoneCorrection(Math.max(robot.gamepad1.triggerLeft, robot.gamepad1.triggerRight));
                if (robot.gamepad1.yButton.toggle) {
                    motorPowers = robot.drive.calcMotorPowers(robot.gamepad1.leftStickX * sensitivityHighPower, robot.gamepad1.leftStickY * sensitivityHighPower, triggerHit * sensitivityHighPower);
                } else {
                    motorPowers = robot.drive.calcMotorPowers(robot.gamepad1.leftStickX * sensitivityLowPower, robot.gamepad1.leftStickY * sensitivityLowPower, triggerHit * sensitivityLowPower);
                }
                robot.drive.setDrivePowers(motorPowers);

                robot.control.setLinearSlideMotorPower(robot.gamepad1.rightStickY);

                if (robot.gamepad1.aButton.isPressed()) {
                    robot.control.openClaw();
                }
                if (robot.gamepad1.bButton.isPressed()) {
                    robot.control.closeClaw();
                }

                if(robot.gamepad1.bumperRight.isPressed()) {
                    robot.control.runIntake();
                }

                if(robot.gamepad1.bumperLeft.isPressed()) {
                    robot.control.stopIntake();
                }

                // Paper Drone
                if (robot.gamepad1.dPadRight.isPressed()) {
                    robot.control.setAirplaneAngle();
                    robot.control.airplaneLaunch();
                }

                if ((robot.gamepad1.bButton.isPressed() && robot.gamepad1.yButton.isPressed()) || (robot.gamepad2.bButton.isPressed() && robot.gamepad2.yButton.isPressed())) {
                    twoGamepads = true;
                }
            }

            Thread.sleep(10); // Ten milli sleep so that the CPU doesn't die (this also means 10 ms baseline lag)
        }
    }
}
