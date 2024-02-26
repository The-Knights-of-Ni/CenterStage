package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import androidx.annotation.Nullable;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Geometry.Path;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller.ControllerOutput;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller.HolonomicPositionController;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller.PositionController;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Localizer.MecanumLocalizer;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotionProfile.MotionProfile;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.PoseEstimation.IMU;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.PoseEstimation.Odometry;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.PoseEstimation.PoseEstimationMethod;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Targeter.PurePursuit;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Targeter.StaticTargeter;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Targeter.Targeter;
import org.firstinspires.ftc.teamcode.Subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Vector;

/**
 * Mecanum drivetrain subsystem
 */
public class Drive extends Subsystem {
    // mm per inch
    public static double mmPerInch = 25.4;
    public static double PURE_PURSUIT_LOOKAHEAD_DISTANCE = 100;

    // DO WITH ENCODERS
    public static double DRIVE_GEAR_REDUCTION = 1.0; // This is < 1.0 if geared UP
    public static double TICKS_PER_MOTOR_REV_20 = 537.6; // AM Orbital 20 motor
    public static double RPM_MAX_NEVERREST_20 = 340;
    public static double ANGULAR_V_MAX_NEVERREST_20 = (TICKS_PER_MOTOR_REV_20 * RPM_MAX_NEVERREST_20) / 60.0;
    // NEW Chassis
    public static double MOTOR_TICK_PER_REV_YELLOW_JACKET_312 = 537.6;
    public static double GOBUILDA_MECANUM_DIAMETER_MM = 96.0;
    public static double COUNTS_PER_MM =
            (MOTOR_TICK_PER_REV_YELLOW_JACKET_312 * DRIVE_GEAR_REDUCTION)
                    / (GOBUILDA_MECANUM_DIAMETER_MM * Math.PI);
    public static double WHEEL_DIAMETER_MM = 100.0;
    public static double WHEEL_DIAMETER_INCHES = WHEEL_DIAMETER_MM / mmPerInch; // For calculating circumference

    public static double COUNTS_PER_INCH =
            (TICKS_PER_MOTOR_REV_20 * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * Math.PI);
    public static double COUNTS_CORRECTION_X = 1.37;
    public static double COUNTS_CORRECTION_Y = 1.0;
    public static double COUNTS_PER_DEGREE = 1180.0 / 90; // 1000 ticks per 90 degrees

    // Move PID coefficients
    public static PIDCoefficients xyPIDCoefficients = new PIDCoefficients(0.0025, 0.000175, 0.0003); // TODO: calibrate
    public static PIDCoefficients thetaPIDCoefficients = new PIDCoefficients(0.00010, 0.000500, 0.00015); // TODO: calibrate
    // Drive-train motors
    private final MecanumLocalizer localizer;
    public PoseEstimationMethod poseEstimator;
    public static Pose currentPose = new Pose(0, 0, 0);


    /**
     * Initializes the drive subsystem, and all related instance variables.
     * @param motors This is a data structure that stores the 4 drive motors for the wheels, similar
     *               to an array but with more functionalities.
     * @param odometry A representation of the odometry wheels, which are extra wheels not used to
     *                 drive the robot but only to estimate the robot's position
     * @param poseEstimationMethodChoice The choice of how to estimate a pose, which represents the
     *                                   robot's position
     * @param imu An imu is a gyro with an accelerometer that it built into the hub. However, our
     *            team does not typically use it, due to its inaccuracy.
     * @param telemetry Telemetry is one way to log what's going on as the code is executing, so
     *                  that the driver can see these notifications.
     */
    public Drive(MotorGeneric<DcMotorEx> motors, @Nullable DcMotorEx[] odometry, PoseEstimationMethodChoice poseEstimationMethodChoice, BNO055IMU imu, Telemetry telemetry) {
        /*Because this is a subclass, it must call its superclass, which is the abstract class
        Subsystem.java. It also takes in a tag, "drive" in this case, that is used to identify that
        this is from drive when messages are displayed to the logs.*/
        super(telemetry, "drive");

        /*Establishes a poseEstimator with the given choice of how pose estimation should be done,
        as given by the parameter poseEstimationMethodChoice*/
        if (poseEstimationMethodChoice == PoseEstimationMethodChoice.IMU) {
            this.poseEstimator = new IMU(imu);
        } else if (poseEstimationMethodChoice == PoseEstimationMethodChoice.ODOMETRY) {
            if (odometry == null) {
                throw new IllegalArgumentException("Odometry is null, but pose estimation method is ODOMETRY");
            }
            this.poseEstimator = new Odometry(odometry[0], odometry[1], odometry[2]);
        } else if (poseEstimationMethodChoice == PoseEstimationMethodChoice.NONE) {
            this.poseEstimator = null;
        } else {
            throw new IllegalArgumentException("Pose estimation method not implemented");
        }

        /*Sets the instance variable localizer equal to a new MecanumLocalizer. A MecanumLocalizer
        ultimately is what controls all motors, by converting a vector into motor powers.*/
        localizer = new MecanumLocalizer(motors.frontLeft, motors.frontRight, motors.rearLeft, motors.rearRight);

        //Motors will brake/stop when power is set to zero (locks the motors, so they don't roll)
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Initializes motor directions. Motor directions default to foward, but we want them to be in reverse
        this.localizer.frontLeft.setDirection(DcMotorEx.Direction.REVERSE);
        this.localizer.rearRight.setDirection(DcMotorEx.Direction.REVERSE);
    }

    /**
     * Sets the zero power behavior (what motors will do when their power is 0) of all drive motors
     * @param mode The zero power mode, choosing from the enums UNKNOWN, BREAK, AND FLOAT
     * @see DcMotorEx#setZeroPowerBehavior(DcMotor.ZeroPowerBehavior)
     * @see DcMotor.ZeroPowerBehavior
     */
    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior mode) {
        this.localizer.frontLeft.setZeroPowerBehavior(mode);
        this.localizer.frontRight.setZeroPowerBehavior(mode);
        this.localizer.rearLeft.setZeroPowerBehavior(mode);
        this.localizer.rearRight.setZeroPowerBehavior(mode);
    }

    /**
     * Sets the run mode (the way in which the motors will run, including RUN_USING_ENCODER AND
     * RUN_TO_POSITION) of all drive motors
     * @param mode The enum run mode
     * @see DcMotorEx#setMode(DcMotor.RunMode)\
     * @see DcMotor.RunMode
     */
    public void setRunMode(DcMotor.RunMode mode) {
        this.localizer.frontLeft.setMode(mode);
        this.localizer.frontRight.setMode(mode);
        this.localizer.rearLeft.setMode(mode);
        this.localizer.rearRight.setMode(mode);
    }

    /**
     * Overloaded method - this version sets the power of each driving motor individually to a
     * (potentially) different power.
     * @param powers The powers to set each of the motors to, stored in a data structure called a
     *               MotorGeneric, which functions similarly to an array but with more features
     * @see DcMotorEx#setPower(double)
     * @see MotorGeneric
     */
    public void setDrivePowers(MotorGeneric<Double> powers) {
        this.localizer.frontLeft.setPower(powers.frontLeft);
        this.localizer.frontRight.setPower(powers.frontRight);
        this.localizer.rearLeft.setPower(powers.rearLeft);
        this.localizer.rearRight.setPower(powers.rearRight);
    }

    /**
     * Overloaded method - this method sets the power of all drive motors to be the same.
     * @param power The power all motors should be set to
     */
    public void setDrivePowers(double power) {
        setDrivePowers(new MotorGeneric<>(power, power, power, power));
    }

    /**
     * Stops the robot by setting all drive motor powers to zero
     */
    private void stop() {
        setDrivePowers(0.0);
    }

    /**
     * Calculates the motor powers based on the given positions of the left and right sticks of
     * gamepad 1.
     * @param leftStickX  The x-position of the left
     * @param leftStickY  The y-position of the left joystick
     * @param rightStickX The x-position of the right joystick (used for turning)
     * @return A list containing each the powers of the four driving motors, in the order front
     * left, front right, rear left, rear right.
     */
    public MotorGeneric<Double> calcMotorPowers(double leftStickX, double leftStickY, double rightStickX) {
        return localizer.localize(new ControllerOutput(leftStickX, leftStickY, rightStickX, 0));
    }

    public void motorController(Targeter targeter, PositionController positionController) {
        poseEstimator.start();
        // Makes sure that the starting tick count is 0 (just in case we're using dead reckoning, which relies on tick counts from the motor encoders)
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // Timeout manager for when the robot gets stuck
        TimeoutManager timeoutManager = new TimeoutManager(100_000_000);
        int timeOutThreshold = 3; // If the encoder does not change by at least this number of ticks, the motor is "stuck"
        currentPose = poseEstimator.getPose();
        var previousPosition = currentPose;
        MotorGeneric<Integer> previousTickCounts = new MotorGeneric<>(0, 0, 0, 0);
        MotorGeneric<Integer> currentTickCounts;
        while (!targeter.reachedTarget(currentPose) && (!timeoutManager.isExceeded())) {
            // Approximates the current position (odometry or dead reckoning) it should be reasonably accurate
            poseEstimator.update();
            currentPose = poseEstimator.getPose();
            // Feeds pose into targeter to get target ...
            var targetPose = targeter.getTarget(currentPose);
            logger.debug("Current", currentPose);
            logger.debug("Target", targetPose);
            logger.debug("Heading", currentPose.heading);
            if (Math.abs(currentPose.heading - previousPosition.heading) > 25) {
                positionController.resetHeadingPID();
            }
            // Feeds target into controller to get motor powers
            localizer.setPowers(positionController.calculate(currentPose, targetPose));
            // Checks if the robot is stuck
            currentTickCounts = new MotorGeneric<>(localizer.frontLeft.getCurrentPosition(), localizer.frontRight.getCurrentPosition(), localizer.rearLeft.getCurrentPosition(), localizer.rearRight.getCurrentPosition());
            if (Math.abs(currentTickCounts.frontLeft - previousTickCounts.frontLeft) > timeOutThreshold || Math.abs(currentTickCounts.frontRight - previousTickCounts.frontRight) > timeOutThreshold || Math.abs(currentTickCounts.rearLeft - previousTickCounts.rearLeft) > timeOutThreshold || Math.abs(currentTickCounts.rearRight - previousTickCounts.rearRight) > timeOutThreshold) {
                timeoutManager.start();
            } else {
                timeoutManager.stop();
            }
            previousTickCounts = currentTickCounts;
            previousPosition = currentPose;
        }
        poseEstimator.stop();
        stop(); // Stops the robot
    }


    public static class TimeoutManager {
        private final long timeout;
        private final ElapsedTime timer;
        private boolean isStarted = false;
        private long startTime = 0;
        private boolean isExceeded = false;

        public TimeoutManager(long timeout) {
            this.timeout = timeout;
            this.timer = new ElapsedTime();
        }

        public void start() {
            isStarted = true;
            startTime = timer.nanoseconds();
        }

        public void stop() {
            isStarted = false;
        }

        public boolean isExceeded() {
            if (isStarted) {
                if (timer.nanoseconds() - startTime > timeout) {
                    isExceeded = true;
                }
            }
            return isExceeded;
        }
    }

    private HolonomicPositionController getHolonomicController() {
        return new HolonomicPositionController(new PID(xyPIDCoefficients), new PID(xyPIDCoefficients), new PID(thetaPIDCoefficients));
    }


    public void move(Pose p) {
        motorController(new StaticTargeter(new Pose(p.x, p.y, p.heading)), getHolonomicController());
    }

    public void moveVector(Vector vector) {
        move(new Pose(vector, 0));
    }

    /**
     * @param vector
     * @param angle
     * @deprecated use {@link #move(Pose)} instead
     */
    @Deprecated
    public void moveVector(Vector vector, double angle) {
        move(new Pose(vector, angle));
    }

    public void moveAngle(int angle) {
        move(new Pose(0, 0, angle));
    }

    public void purePursuit(Path path) {
        motorController(new PurePursuit(path, PURE_PURSUIT_LOOKAHEAD_DISTANCE), getHolonomicController());
    }


    public void followProfile(MotionProfile profile, PositionController positionController) {
        poseEstimator.start();
        var timeoutManager = new TimeoutManager(100_000_000);
        var timer = new ElapsedTime();
        timer.reset();
        MotorGeneric<Integer> previousTickCounts = new MotorGeneric<>(0, 0, 0, 0);
        MotorGeneric<Integer> currentTickCounts;
        int timeOutThreshold = 3; // If the encoder does not change by at least this number of ticks, the motor is "stuck"
        currentPose = poseEstimator.getPose();
        while (!profile.isFinished(timer.seconds()) && !timeoutManager.isExceeded()) {
            poseEstimator.update();
            currentPose = poseEstimator.getPose();
            var target = profile.calculate(timer.seconds());
            var positionMotorPowers = positionController.calculate(currentPose, new Pose(target.x.position,
                    target.y.position,
                    target.heading.position)); // TODO: deal with angles properly
            localizer.setPowers(positionMotorPowers);
            currentTickCounts = new MotorGeneric<>(localizer.frontLeft.getCurrentPosition(), localizer.frontRight.getCurrentPosition(), localizer.rearLeft.getCurrentPosition(), localizer.rearRight.getCurrentPosition());
            if (Math.abs(currentTickCounts.frontLeft - previousTickCounts.frontLeft) > timeOutThreshold || Math.abs(currentTickCounts.frontRight - previousTickCounts.frontRight) > timeOutThreshold || Math.abs(currentTickCounts.rearLeft - previousTickCounts.rearLeft) > timeOutThreshold || Math.abs(currentTickCounts.rearRight - previousTickCounts.rearRight) > timeOutThreshold) {
                timeoutManager.start();
            } else {
                timeoutManager.stop();
            }
            previousTickCounts = currentTickCounts;
        }
        poseEstimator.stop();
    }
}
