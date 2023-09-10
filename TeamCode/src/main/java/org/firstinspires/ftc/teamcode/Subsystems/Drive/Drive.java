package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import android.util.Log;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.Util.Vector;

/**
 * Mecanum drivetrain subsystem
 */
public class Drive extends Subsystem {
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
    private static final double COUNTS_PER_DEGREE = 1180/90; // 1000 ticks per 90 degrees

    // Default drive speeds
    private static final double DRIVE_SPEED = 0.60;
    private static final double DRIVE_SPEED_X = 0.70;
    private static final double DRIVE_SPEED_Y = 0.80;
    private static final double TURN_SPEED = 0.40;

    // PID Constants
    private static final double motorKp = 0.0025;
    private static final double motorKi = 0.000175;
    private static final double motorKd = 0.0003;

    // Drive-train motors
    public final DcMotorEx frontLeft;
    public final DcMotorEx frontRight;
    public final DcMotorEx rearLeft;
    public final DcMotorEx rearRight;

    // PID Controllers
    public MoveSystem flControl;
    public MoveSystem frControl;
    public MoveSystem rlControl;
    public MoveSystem rrControl;

    // State variables for robot position
    private double robotX;
    private double robotY;
    private double robotTheta;

    private final ElapsedTime timer;
    private long startTime;

    public final boolean odometryEnabled; // TODO: Implement

    /**
     * Initializes the drive subsystem
     *
     * @param frontLeft   The front left motor in the drive train
     * @param frontRight  The front right motor
     * @param rearLeft    The rear left motor
     * @param rearRight   The rear right motor
     * @param telemetry   The telemetry
     * @param elapsedTime The timer for the elapsed time
     */
    public Drive(DcMotorEx frontLeft, DcMotorEx frontRight, DcMotorEx rearLeft, DcMotorEx rearRight, boolean odometryEnabled, Telemetry telemetry, ElapsedTime elapsedTime) {
        super(telemetry, "drive");
        this.timer = elapsedTime;
        this.odometryEnabled = odometryEnabled;
        // Initialize motors
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.rearLeft = rearLeft;
        this.rearRight = rearRight;

        // Motors will brake/stop when power is set to zero (locks the motors so they don't roll around)
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initialize robot position
        this.robotX = 0;
        this.robotY = 0;
        this.robotTheta = 0;
    }

    /**
     * Uniformly sets zero power behavior of all drive motors
     *
     * @param mode
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
     * @param mode
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
     */
    public void setDrivePowers(double[] powers) {
        frontLeft.setPower(powers[0]);
        frontRight.setPower(powers[1]);
        rearLeft.setPower(powers[2]);
        rearRight.setPower(powers[3]);
    }

    /**
     * Sets all drive motor powers to zero
     */
    private void stop() {
        setDrivePowers(new double[] {0, 0, 0, 0});
    }

    /**
     * Calculates the motor powers when given the position o the left and right sticks
     *
     * @param leftStickX  left joystick x position
     * @param leftStickY  left joystick y position
     * @param rightStickX right joystick x position for turning
     * @return A list with the motor powers
     */
    public double[] calcMotorPowers(double leftStickX, double leftStickY, double rightStickX) {
        double r = Math.hypot(leftStickX, leftStickY);
        double robotAngle = Math.atan2(leftStickY, leftStickX) - Math.PI / 4;
        double lrPower = r * Math.sin(robotAngle) + rightStickX;
        double lfPower = r * Math.cos(robotAngle) + rightStickX;
        double rrPower = r * Math.cos(robotAngle) - rightStickX;
        double rfPower = r * Math.sin(robotAngle) - rightStickX;
        return new double[]{lfPower, rfPower, lrPower, rrPower};
    }

    /**
     * PID motor control program to ensure all four motors are synchronized
     *
     * @param tickCount How far each motor should go
     */
    public void allMotorControl(int[] tickCount, MoveSystem[] moveSystems) {
        // Refresh motors
        stop();
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        boolean initialized = false;

        // Initialize PID controllers
        this.flControl = moveSystems[0];
        this.frControl = moveSystems[1];
        this.rlControl = moveSystems[2];
        this.rrControl = moveSystems[3];

        // Current motor encoder values
        int currentCountFL;
        int currentCountFR;
        int currentCountRL;
        int currentCountRR;

        // Previous motor encoder values
        int prevCountFL = 0;
        int prevCountFR = 0;
        int prevCountRL = 0;
        int prevCountRR = 0;

        // Conditionals to control PID loop
        boolean isMotorFLDone = false;
        boolean isMotorFRDone = false;
        boolean isMotorRLDone = false;
        boolean isMotorRRDone = false;

        // Conditionals to control timeout
        boolean isMotorFLNotMoving = false;
        boolean isMotorFRNotMoving = false;
        boolean isMotorRLNotMoving = false;
        boolean isMotorRRNotMoving = false;

        // Timeout control (stop loop if motor stalls)
        boolean isTimeOutStarted = false;
        boolean isTimeOutExceeded = false;
        double timeOutPeriod = 0.1;
        double timeOutStartedTime = 0.0;
        int timeOutThreshold = 3; // If the encoder does not change by 2 ticks, motor is "stuck"
        double currentTime = 0.0;

        while(((!isMotorFLDone) || (!isMotorFRDone) || (!isMotorRLDone) || (!isMotorRRDone)) && (!isTimeOutExceeded)) {
            // Update current variables
            currentTime = ((double) timer.nanoseconds()) * 1.0e-9 - startTime;
            currentCountFL = frontLeft.getCurrentPosition();
            currentCountFR = (int) (frontRight.getCurrentPosition() / 0.7); // FR is always off, not sure why
            currentCountRL = rearLeft.getCurrentPosition();
            currentCountRR = rearRight.getCurrentPosition();

            // PID control
            double powerFL = flControl.calculate(tickCount[0], currentCountFL);
            double powerFR = frControl.calculate(tickCount[1], currentCountFR);
            double powerRL = rlControl.calculate(tickCount[2], currentCountRL);
            double powerRR = rrControl.calculate(tickCount[3], currentCountRR);
            frontLeft.setPower(DRIVE_SPEED * powerFL);
            frontRight.setPower(DRIVE_SPEED * powerFR);
            rearLeft.setPower(DRIVE_SPEED * powerRL);
            rearRight.setPower(DRIVE_SPEED * powerRR);
            // Check for target hit
            int directionSign;
            directionSign = tickCount[0] / Math.abs(tickCount[0]);
            if (tickCount[0] == 0 || currentCountFL * directionSign >= Math.abs(tickCount[0])) {
                isMotorFLDone = true;
                isMotorFLNotMoving = true;
                frontLeft.setPower(0.0);
            }
            directionSign = tickCount[1] / Math.abs(tickCount[1]);
            if (tickCount[1] == 0 || currentCountFR * directionSign >= Math.abs(tickCount[1])) {
                isMotorFRDone = true;
                isMotorFRNotMoving = true;
                frontRight.setPower(0.0);
            }
            directionSign = tickCount[2] / Math.abs(tickCount[2]);
            if (tickCount[2] == 0 || currentCountRL * directionSign >= Math.abs(tickCount[2])) {
                isMotorRLDone = true;
                isMotorRLNotMoving = true;
                rearLeft.setPower(0.0);
            }
            directionSign = tickCount[3] / Math.abs(tickCount[3]);
            if (tickCount[3] == 0 || currentCountRR * directionSign >= Math.abs(tickCount[3])) {
                isMotorRRDone = true;
                isMotorRRNotMoving = true;
                rearRight.setPower(0.0);
            }

            // Check for timeout
            if (initialized) { // check if the motor is rotating
                isMotorFLNotMoving = Math.abs(currentCountFL - prevCountFL) < timeOutThreshold;
                isMotorFRNotMoving = Math.abs(currentCountFR - prevCountFR) < timeOutThreshold;
                isMotorRLNotMoving = Math.abs(currentCountRL - prevCountRL) < timeOutThreshold;
                isMotorRRNotMoving = Math.abs(currentCountRR - prevCountRR) < timeOutThreshold;
            }
            if (isMotorFLNotMoving && isMotorFRNotMoving && isMotorRLNotMoving && isMotorRRNotMoving) {
                if (isTimeOutStarted) {
                    if (currentTime - timeOutStartedTime > timeOutPeriod) {
                        isTimeOutExceeded = true;
                    }
                } else { // time out was not started yet
                    isTimeOutStarted = true;
                    timeOutStartedTime = currentTime;
                }
            } else {
                isTimeOutStarted = false;
                isTimeOutExceeded = false;
            }

            prevCountFL = currentCountFL;
            prevCountFR = currentCountFR;
            prevCountRL = currentCountRL;
            prevCountRR = currentCountRR;

            initialized = true;
            Log.d("Target tick", tickCount[0] + " " + tickCount[1] + " " + tickCount[2] + " " + tickCount[3]);
            Log.d("Current tick", currentCountFL + " " + currentCountFR + " " + currentCountRL + " " + currentCountRR);
            Log.d("Current power", powerFL + " " + powerFR + " " + powerRL + " " + powerRR);
        }
    }

    public void moveVector(Vector v) {
        moveVector(v, 0);
    }

    public void moveVector(Vector v, double turnAngle) {
        Vector newV = new Vector(v.getX() * COUNTS_PER_MM * COUNTS_CORRECTION_X, v.getY() * COUNTS_PER_MM * COUNTS_CORRECTION_Y);
        // Sqrt2 is introduced as a correction factor, since the pi/4 in the next line is required
        // for the strafer chassis to operate properly
        double distance = newV.distance(Vector2D.ZERO) * Math.sqrt(2);
        double angle = Math.atan2(newV.getY(), newV.getX()) - Math.PI / 4;

        int[] tickCount = new int[4]; // All tick counts need to be integers
        tickCount[0] = (int)((distance * Math.cos(angle)));
        tickCount[0] -= (int)(turnAngle * COUNTS_PER_DEGREE);
        tickCount[1] = (int)((distance * Math.sin(angle)));
        tickCount[1] += (int)(turnAngle * COUNTS_PER_DEGREE);
        tickCount[2] = (int)((distance * Math.sin(angle)));
        tickCount[2] -= (int)(turnAngle * COUNTS_PER_DEGREE);
        tickCount[3] = (int)((distance * Math.cos(angle)));
        tickCount[3] += (int)(turnAngle * COUNTS_PER_DEGREE);
        MoveSystem[] pids = {new PID(motorKp, motorKi, motorKd), new PID(motorKp, motorKi, motorKd), new PID(motorKp, motorKi, motorKd)};
        allMotorControl(tickCount, pids);
        stop();
    }
}
