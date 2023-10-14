package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.GamepadWrapper;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Subsystems.Control.Control.CraneState;
import org.firstinspires.ftc.teamcode.Subsystems.Control.ScorePixelThread;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Drive;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotorGeneric;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.HashMap;

@TeleOp(name = "TeleOp")
public class Teleop extends LinearOpMode {
    double deltaT;
    double timeCurrent;
    double timePre;
    ElapsedTime timer;
    private Robot robot;

    private void initOpMode() {
        // Initialize DC motor objects
        timer = new ElapsedTime();
        HashMap<String, Boolean> flags = new HashMap<>();
        flags.put("web", true);
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
        robot.control.initDevicesTeleop();
        waitForStart();

        telemetry.clearAll();
        timeCurrent = timer.nanoseconds();
        timePre = timeCurrent;

        final double sensitivityHighPower = 1.0; // multiply inputs with this on high power mode
        final double sensitivityLowPower = 0.7; // multiply inputs with this on non-high power mode
        var twoGamepads = true;

        while (opModeIsActive()) {
            Robot.updateGamepads();

            timeCurrent = timer.nanoseconds();
            deltaT = timeCurrent - timePre;
            timePre = timeCurrent;
            if (twoGamepads) {
                MotorGeneric<Double> motorPowers;
                if (Robot.gamepad1.yButton.toggle) {
                    motorPowers = robot.drive.calcMotorPowers(Robot.gamepad1.leftStickX * sensitivityHighPower, Robot.gamepad1.leftStickY * sensitivityHighPower, Robot.gamepad1.rightStickX * sensitivityHighPower);
                } else {
                    motorPowers = robot.drive.calcMotorPowers(Robot.gamepad1.leftStickX * sensitivityLowPower, Robot.gamepad1.leftStickY * sensitivityLowPower, Robot.gamepad1.rightStickX * sensitivityLowPower);
                }

                robot.drive.setDrivePowers(motorPowers);

                // Close claw and score ...
                if (Robot.gamepad1.aButton.isPressed()) {
                    new ScorePixelThread(robot.control).start();
                }

                // Paper Drone
                if (Robot.gamepad1.dPadUp.isPressed()) {
                    robot.control.airplaneLaunch(PlaneLaunchRange.MEDIUM);
                } else if (Robot.gamepad1.dPadRight.isPressed()) {
                    robot.control.airplaneLaunch(PlaneLaunchRange.SHORT);
                } else if (Robot.gamepad1.dPadLeft.isPressed()) {
                    robot.control.airplaneLaunch(PlaneLaunchRange.LONG);
                }
                if (Robot.gamepad1.dPadDown.isPressed()) {
                    robot.control.airplaneLaunch(PlaneLaunchRange.OFF);
                }


                // Claw
                if (Robot.gamepad2.aButton.isPressed()) {
                    robot.control.openClaw();
                }
                if (Robot.gamepad2.bButton.isPressed()) {
                    robot.control.closeClaw();
                }

                // Crane
                if (Robot.gamepad2.xButton.isPressed()) {
                    robot.control.moveCrane(CraneState.DOWN);
                }
                if (Robot.gamepad2.yButton.isPressed()) {
                    robot.control.moveCrane(CraneState.UP);
                }

                // Linear Slide
                if (Robot.gamepad2.triggerLeft > 0.15 || Robot.gamepad2.triggerRight > 0.15) {
                    if (Robot.gamepad2.triggerRight > Robot.gamepad2.triggerLeft) {
                        robot.control.setLinearSlideMotorPower(Robot.gamepad2.triggerRight);
                    } else if (Robot.gamepad2.triggerLeft > Robot.gamepad2.triggerRight) {
                        robot.control.setLinearSlideMotorPower(-Robot.gamepad2.triggerLeft);
                    }
                }

                // April Tag Correction
                if (Robot.gamepad2.dPadLeft.isPressed()) {
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
                }

                // Switch to one gamepad
                if ((Robot.gamepad1.bButton.isPressed() && Robot.gamepad1.xButton.isPressed()) || (Robot.gamepad2.bButton.isPressed() && Robot.gamepad2.xButton.isPressed())) {
                    twoGamepads = false;
                }

            } else {
                // Must use gamepad 1 for one gamepad TODO: Find elegant fix
                MotorGeneric<Double> motorPowers;
                double triggerHit = GamepadWrapper.joystickDeadzoneCorrection(Math.max(Robot.gamepad1.triggerLeft, Robot.gamepad1.triggerRight));
                if (Robot.gamepad1.yButton.toggle) {
                    motorPowers = robot.drive.calcMotorPowers(Robot.gamepad1.leftStickX * sensitivityHighPower, Robot.gamepad1.leftStickY * sensitivityHighPower, triggerHit * sensitivityHighPower);
                } else {
                    motorPowers = robot.drive.calcMotorPowers(Robot.gamepad1.leftStickX * sensitivityLowPower, Robot.gamepad1.leftStickY * sensitivityLowPower, triggerHit * sensitivityLowPower);
                }
                robot.drive.setDrivePowers(motorPowers);

                robot.control.setLinearSlideMotorPower(Robot.gamepad1.rightStickY);

                if (Robot.gamepad1.aButton.isPressed()) {
                    robot.control.openClaw();
                }
                if (Robot.gamepad1.bButton.isPressed()) {
                    robot.control.closeClaw();
                }

                // Paper Drone
                if (Robot.gamepad1.dPadUp.isPressed()) {
                    robot.control.airplaneLaunch(PlaneLaunchRange.MEDIUM);
                } else if (Robot.gamepad1.dPadRight.isPressed()) {
                    robot.control.airplaneLaunch(PlaneLaunchRange.SHORT);
                } else if (Robot.gamepad1.dPadLeft.isPressed()) {
                    robot.control.airplaneLaunch(PlaneLaunchRange.LONG);
                }
                if (Robot.gamepad1.dPadDown.isPressed()) {
                    robot.control.airplaneLaunch(PlaneLaunchRange.OFF);
                }

                if ((Robot.gamepad1.bButton.isPressed() && Robot.gamepad1.xButton.isPressed()) || (Robot.gamepad2.bButton.isPressed() && Robot.gamepad2.xButton.isPressed())) {
                    twoGamepads = true;
                }
            }

            Thread.sleep(10); // Ten milli sleep so that the CPU doesn't die (this also means 10 ms baseline lag)
        }
    }
}
