package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.GamepadWrapper;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotorGeneric;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Robot;

import java.util.HashMap;
import java.util.List;

import static java.lang.Math.abs;

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

        // Activates bulk reading, a faster way of reading data
        List<LynxModule> allHubs = robot.hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        ElapsedTime timer = new ElapsedTime();
        robot.control.initDevicesTeleop();
        waitForStart();

        telemetry.clearAll();
        timeCurrent = timer.nanoseconds();
        timePre = timeCurrent;

        final double sensitivityHighPower = 1.0; // multiply inputs with this on high power mode
        final double sensitivityLowPower = 0.5; // multiply inputs with this on non-high power mode
        var twoGamepads = true;
        double slidePowerVel;
        double cranePowerVel;

        while (opModeIsActive()) {
            //clears cache to prevent overflow
            for (LynxModule hub : allHubs) {
                hub.clearBulkCache();
            }

            //get data from gamepads
            Robot.updateGamepads();

            //get current time
            timeCurrent = timer.nanoseconds();
            deltaT = timeCurrent - timePre;
            timePre = timeCurrent;

            //gets the motor powers for drive from gamepad1
            //y button activates low speed mode
            //it gets the x and y positioning from the left stick and turns based on the right stick's x
            //calcMotorPowers creates a MotorGeneric
            if (twoGamepads) {
                MotorGeneric<Double> motorPowers;
                if (!Robot.gamepad1.yButton.toggle) {
                    motorPowers = robot.drive.calcMotorPowers(sensitivityHighPower * Robot.gamepad1.leftStickX, sensitivityHighPower * robot.gamepad1.leftStickY, sensitivityHighPower * robot.gamepad1.rightStickX);
                } else {
                    motorPowers = robot.drive.calcMotorPowers(sensitivityLowPower * Robot.gamepad1.leftStickX, sensitivityLowPower * robot.gamepad1.leftStickY, sensitivityLowPower * robot.gamepad1.rightStickX);
                }

                //gets the robot to actually move from the newly created MotorGeneric
                robot.drive.setDrivePowers(motorPowers);

                // Paper Drone

                if (Robot.gamepad2.dPadLeft.isPressed()) {
                    robot.control.airplaneLaunch();
                }

                if (Robot.gamepad2.bumperRight.isPressed()) {
                    robot.control.setAirplaneAngle();
                }

                if (Robot.gamepad2.bumperLeft.isPressed()) {
                    robot.control.resetAirplaneAngle();
                }

                if (Robot.gamepad1.bumperRight.isPressed()) {
                    robot.control.runIntake();
                }

                if (Robot.gamepad1.bumperLeft.isPressed()) {
                    robot.control.stopIntake();
                }

                // Claw
                if (Robot.gamepad2.aButton.isPressed()) {
                    robot.control.openClaw();
                }
                if (Robot.gamepad2.bButton.isPressed()) {
                    robot.control.closeClaw();
                }
                if (Robot.gamepad2.dPadUp.isPressed()) {
                    robot.control.extendShoulder();
                }
                if (Robot.gamepad2.dPadRight.isPressed()) {
                    robot.control.retractShoulder();
                }
                if (Robot.gamepad2.dPadDown.isPressed()) {
                    robot.control.pickupPosShoulder();
                }

                // Linear Slide
                // TODO: Implement auto correcting of claw shoulder
                slidePowerVel = Robot.gamepad2.triggerLeft - Robot.gamepad2.triggerRight;
                if (abs(slidePowerVel) >= 0.15) {
                    robot.control.setCraneLinearSlideMotorPower(slidePowerVel);
                } else {
                    robot.control.setCraneLinearSlideMotorPower(0);
                }
                cranePowerVel = Robot.gamepad1.triggerLeft - Robot.gamepad1.triggerRight;
                if (abs(cranePowerVel) >= 0.10) {
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

                robot.control.setCraneLinearSlideMotorPower(Robot.gamepad1.rightStickY);

                if (Robot.gamepad1.aButton.isPressed()) {
                    robot.control.openClaw();
                }
                if (Robot.gamepad1.bButton.isPressed()) {
                    robot.control.closeClaw();
                }

                if (Robot.gamepad1.bumperRight.isPressed()) {
                    robot.control.runIntake();
                }

                if (Robot.gamepad1.bumperLeft.isPressed()) {
                    robot.control.stopIntake();
                }

                // Paper Drone
                if (Robot.gamepad1.dPadRight.isPressed()) {
                    robot.control.setAirplaneAngle();
                    robot.control.airplaneLaunch();
                }

                if ((Robot.gamepad1.bButton.isPressed() && Robot.gamepad1.yButton.isPressed()) || (Robot.gamepad2.bButton.isPressed() && Robot.gamepad2.yButton.isPressed())) {
                    twoGamepads = true;
                }
            }

            Thread.sleep(5); // Ten milli sleep so that the CPU doesn't die (this also means 10 ms baseline lag)
        }
    }
}
