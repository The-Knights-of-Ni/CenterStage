package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import android.util.Log;

import androidx.annotation.Nullable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Web.WebAction;
import org.firstinspires.ftc.teamcode.Subsystems.Web.WebThread;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.Arrays;
import java.util.Locale;

/**
 * Mecanum drivetrain subsystem
 * @deprecated Use {@link Drive} instead
 */
@Deprecated
public class OldDrive extends Subsystem {
    // mm per inch
    public static final double mmPerInch = 25.4;

    // DO WITH ENCODERS
    private static final double DRIVE_GEAR_REDUCTION = 1.0; // This is < 1.0 if geared UP
    private static final double TICKS_PER_MOTOR_REV_20 = 537.6; // AM Orbital 20 motor
    private static final double RPM_MAX_NEVERREST_20 = 340;
    private static final double ANGULAR_V_MAX_NEVERREST_20 = (TICKS_PER_MOTOR_REV_20 * RPM_MAX_NEVERREST_20) / 60.0;
    // NEW Chassis
    private static final double MOTOR_TICK_PER_REV_YELLOW_JACKET_312 = 537.6;
    private static final double GOBUILDA_MECANUM_DIAMETER_MM = 96.0;
    private static final double COUNTS_PER_MM =
            (MOTOR_TICK_PER_REV_YELLOW_JACKET_312 * DRIVE_GEAR_REDUCTION)
                    / (GOBUILDA_MECANUM_DIAMETER_MM * Math.PI);
    private static final double WHEEL_DIAMETER_MM = 100.0;
    private static final double WHEEL_DIAMETER_INCHES = WHEEL_DIAMETER_MM / mmPerInch; // For calculating circumference

    private static final double COUNTS_PER_INCH =
            (TICKS_PER_MOTOR_REV_20 * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * Math.PI);
    private static final double COUNTS_CORRECTION_X = 1.37;
    private static final double COUNTS_CORRECTION_Y = 1.0;
    private static final double COUNTS_PER_DEGREE = 1180.0 / 90; // 1000 ticks per 90 degrees

    // Default drive speeds
    public static final double DRIVE_SPEED = 0.60;

    // PID Constants
    private static final double motorKp = 0.0025;
    private static final double motorKi = 0.000175;
    private static final double motorKd = 0.0003;

    // Drive-train motors
    public final DcMotorEx frontLeft;
    public final DcMotorEx frontRight;
    public final DcMotorEx rearLeft;
    public final DcMotorEx rearRight;
    public final DcMotorEx[] odometry;
    private final boolean debug = true;
    private final ElapsedTime timer;
    // State variables for robot position
    private final double robotX;
    private final double robotY;
    private final double robotTheta;

    /**
     * Initializes the drive subsystem
     * @param telemetry   The telemetry
     * @param elapsedTime The timer for the elapsed time
     */
    public OldDrive(MotorGeneric<DcMotorEx> motors, @Nullable DcMotorEx[] odometryEnabled, Object imu, Telemetry telemetry, ElapsedTime elapsedTime) {
        super(telemetry, "drive");
        this.timer = elapsedTime;
        this.odometry = odometryEnabled;
        // Set motors
        this.frontLeft = motors.frontLeft;
        this.frontRight = motors.frontRight;
        this.rearLeft = motors.rearLeft;
        this.rearRight = motors.rearRight;

        motors.frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motors.rearRight.setDirection(DcMotorSimple.Direction.REVERSE);

        // Motors will brake/stop when power is set to zero (locks the motors, so they don't roll around)
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initialize robot position
        this.robotX = 0;
        this.robotY = 0;
        this.robotTheta = 0;
    }

    public static boolean isMotorDone(int currentCount, int targetCount) {
        return Math.abs(currentCount - targetCount) < 25 || (Math.abs(currentCount - targetCount) > 50 && Math.abs(currentCount) > Math.abs(targetCount));
    }

    /**
     * Uniformly sets zero power behavior of all drive motors
     *
     * @param mode Zero Power Mode
     * @see DcMotorEx#setZeroPowerBehavior(DcMotor.ZeroPowerBehavior)
     */
    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior mode) {
        this.frontLeft.setZeroPowerBehavior(mode);
        this.frontRight.setZeroPowerBehavior(mode);
        this.rearLeft.setZeroPowerBehavior(mode);
        this.rearRight.setZeroPowerBehavior(mode);
    }

    /**
     * Uniformly sets run mode of all drive motors
     *
     * @param mode Run mode
     * @see DcMotorEx#setMode(DcMotor.RunMode)
     */
    public void setRunMode(DcMotor.RunMode mode) {
        this.frontLeft.setMode(mode);
        this.frontRight.setMode(mode);
        this.rearLeft.setMode(mode);
        this.rearRight.setMode(mode);
    }

    /**
     * Sets the drive power of each motor individually.
     *
     * @param powers the powers to set each of the motors to
     * @see DcMotorEx#setPower(double)
     */
    public void setDrivePowers(double[] powers) {
        frontLeft.setPower(powers[0]);
        frontRight.setPower(powers[1]);
        rearLeft.setPower(powers[2]);
        rearRight.setPower(powers[3]);
    }

    public void setDrivePowers(MotorGeneric<Double> powers) {
        this.frontLeft.setPower(powers.frontLeft);
        this.frontRight.setPower(powers.frontRight);
        this.rearLeft.setPower(powers.rearLeft);
        this.rearRight.setPower(powers.rearRight);
    }

    public void setDrivePowers(double power) {
        setDrivePowers(new MotorGeneric<>(power, power, power, power));
    }

    /**
     * Sets all drive motor powers to zero
     */
    private void stop() {
        setDrivePowers(0.0);
    }

    /**
     * Calculates the motor powers when given the position o the left and right sticks
     *
     * @param leftStickX  left joystick x position
     * @param leftStickY  left joystick y position
     * @param rightStickX right joystick x position for turning
     * @return A list with the motor powers
     */
    public MotorGeneric<Double> calcMotorPowers(double leftStickX, double leftStickY, double rightStickX) {
        var r = Math.hypot(leftStickX, leftStickY);
        var robotAngle = Math.atan2(leftStickY, leftStickX) - Math.PI / 4;
        var lfPower = r * Math.cos(robotAngle) + rightStickX;
        var lrPower = r * Math.sin(robotAngle) + rightStickX;
        var rfPower = r * Math.sin(robotAngle) - rightStickX;
        var rrPower = r * Math.cos(robotAngle) - rightStickX;
        return new MotorGeneric<>(lfPower, rfPower, lrPower, rrPower);
    }


    /**
     * PID motor control program to ensure all four motors are synchronized
     *
     * @param tickCount How far each motor should go
     */
    public void allMotorControl(int[] tickCount, PID[] moveSystems) {
        // Is there a bug in this code? If so, use these debugging steps:
        // 1. Test if the encoders update with EncoderTest.
        // 2. Check if MotorControlData gets the tick count.
        // 3. Make sure the PID works, by looking at the logs.
        // 4. Check if the motors are set to the correct power by adding a temporary debugging log statement.

        logger.info("Moving " + Arrays.toString(tickCount));
        WebThread.addAction(new WebAction("drive", "Moving " + Arrays.toString(tickCount)));
        // Refresh motors
        stop();
        // DO NOT USE RUN_USING_ENCODER OR ANYTHING BUT RUN_WITHOUT_ENCODER as your run mode.
        // This will mess up the PID and might even use the built-in REV PID.
        // This will not allow the algo to set raw motor powers.
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // Makes sure that the starting tick count is 0

        // Timeout control (stop loop if motor stalls)
        long currentTime; // current time in nanoseconds
        long startTime = timer.nanoseconds();
        boolean isTimeOutStarted = false;
        boolean isTimeOutExceeded = false; // If timeout is exceeded pid stops and logs an error
        int timeOutPeriod = 100_000_000;
        double timeOutStartedTime = 0.0;
        // If the encoder does not change by at least this number of ticks, the motor is considered to be stuck
        int timeOutThreshold = 3;

        // Initialize motor data wrappers
        MotorControlData fl = new MotorControlData(frontLeft, moveSystems[0], tickCount[0], timeOutThreshold, logger.telemetry, "frontLeft");
        MotorControlData fr = new MotorControlData(frontRight, moveSystems[1], tickCount[1], timeOutThreshold, logger.telemetry, "frontRight");
        MotorControlData rl = new MotorControlData(rearLeft, moveSystems[2], tickCount[2], timeOutThreshold, logger.telemetry, "rearLeft");
        MotorControlData rr = new MotorControlData(rearRight, moveSystems[3], tickCount[3], timeOutThreshold, logger.telemetry, "rearRight");
        // TODO: This should be a motor generic
        while (((!fl.isDone) || (!fr.isDone) || (!rl.isDone) || (!rr.isDone)) && (!isTimeOutExceeded)) {
            // Update current variables
            currentTime = timer.nanoseconds() - startTime;

            // Run a cycle for each
            fl.cycle();
            fr.cycle();
            rl.cycle();
            rr.cycle();
            if (fl.isNotMoving && fr.isNotMoving && rl.isNotMoving && rr.isNotMoving) {
                if (isTimeOutStarted && currentTime - timeOutStartedTime > timeOutPeriod) {
                    isTimeOutExceeded = true;
                    logger.info("Move failed, timeout exceeded");
                } else { // time out was not started yet
                    isTimeOutStarted = true;
                    timeOutStartedTime = currentTime;
                }
            } else {
                isTimeOutStarted = false;
            }
            if (debug) {
                logger.verbose(Locale.US, "Current Tick Count Check: %d %d %d %d", frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(), rearLeft.getCurrentPosition(), rearRight.getCurrentPosition());
                logger.verbose(Locale.US, "PID Dump: " + fl.moveSystem.toString() + " " + fr.moveSystem.toString() + " " + rl.moveSystem.toString() + " " + rr.moveSystem.toString());
                logger.debug(Locale.US, "Motor Info: %.2f;%d/%d %.2f;%d/%d %.2f;%d/%d %.2f;%d/%d", fl.power, fl.currentCount, fl.targetCount, fr.power, fr.currentCount, fr.targetCount, rl.power, rl.currentCount, rl.targetCount, rr.power, rr.currentCount, rr.targetCount);
            }
        }
        WebThread.removeAction("drive");
    }

    public void moveVector(Vector v) {
        moveVector(v, 0);
    }

    public void moveAngle(double turnAngle) {
        moveVector(new Vector(0, 0), turnAngle);
    }

    public void moveVector(Vector v, double turnAngle) {
        Vector newV = new Vector(v.getX() * COUNTS_CORRECTION_X * COUNTS_PER_MM, v.getY() * COUNTS_CORRECTION_Y * COUNTS_PER_MM);
        // Sqrt2 is introduced as a correction factor, since the pi/4 in the next line is required
        // for the strafer chassis to operate properly
        double distance = newV.distance(Vector2D.ZERO) * Math.sqrt(2);
        double angle = Math.atan2(newV.getY(), newV.getX()) - Math.PI / 4;

        int[] tickCount = new int[4]; // All tick counts need to be integers
        tickCount[0] = (int) ((distance * Math.cos(angle)));
        tickCount[0] -= (int) (turnAngle * COUNTS_PER_DEGREE);
        tickCount[1] = (int) ((distance * Math.sin(angle)));
        tickCount[1] += (int) (turnAngle * COUNTS_PER_DEGREE);
        tickCount[2] = (int) ((distance * Math.sin(angle)));
        tickCount[2] -= (int) (turnAngle * COUNTS_PER_DEGREE);
        tickCount[3] = (int) ((distance * Math.cos(angle)));
        tickCount[3] += (int) (turnAngle * COUNTS_PER_DEGREE);
        PID[] pids = {new PID(motorKp, motorKi, motorKd), new PID(motorKp, motorKi, motorKd), new PID(motorKp, motorKi, motorKd), new PID(motorKp, motorKi, motorKd)};
        allMotorControl(tickCount, pids);
        stop();
    }

    public void move(Pose pose) {
        moveVector(pose.getCoordinate(), pose.heading);
    }
}
