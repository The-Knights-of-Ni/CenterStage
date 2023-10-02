package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import androidx.annotation.Nullable;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Web.WebAction;
import org.firstinspires.ftc.teamcode.Subsystems.Web.WebThread;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.Arrays;

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
    private static final double COUNTS_PER_DEGREE = 1180 / 90; // 1000 ticks per 90 degrees

    // Default drive speeds
    private static final double DRIVE_SPEED = 0.60;

    // PID Constants
    private static final double motorKp = 0.0025;
    private static final double motorKi = 0.000175;
    private static final double motorKd = 0.0003;

    // Drive-train motors
    public final DcMotorEx frontLeft;
    public final DcMotorEx frontRight;
    public final DcMotorEx rearLeft;
    public final DcMotorEx rearRight;
    // Odometry Encoders/Constants
    public final boolean odometryEnabled;
    public DcMotorEx odL;
    public DcMotorEx odB;
    public DcMotorEx odR;
    public final double ODOMETRY_TRACKWIDTH = 10.0; // TODO: Calibrate
    public final double ODOMETRY_BACK_DISPLACEMENT = 10.0; // How far back the back odometry wheel is TODO: Calibrate

    private final boolean debug = false;

    private BNO055IMU imu;
    Pose currentPosition = new Pose(0, 0, 0);
    private int previousLeftOdometryTicks = 0;
    private int previousBackOdometryTicks = 0;
    private int previousRightOdometryTicks = 0;


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
    public Drive(DcMotorEx frontLeft, DcMotorEx frontRight, DcMotorEx rearLeft, DcMotorEx rearRight, @Nullable DcMotorEx[] odometry, BNO055IMU imu, Telemetry telemetry, ElapsedTime elapsedTime) {
        super(telemetry, "drive");
        this.odometryEnabled = odometry != null;
        if (odometryEnabled) {
            this.odL = odometry[0];
            this.odB = odometry[1];
            this.odR = odometry[2];
        } else {
            this.odL = null;
            this.odB = null;
            this.odR = null;
        }
        // Set motors
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.rearLeft = rearLeft;
        this.rearRight = rearRight;
        this.imu = imu;

        // Motors will brake/stop when power is set to zero (locks the motors, so they don't roll around)
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    private static boolean isMotorDone(int currentCount, int targetCount) {
        return Math.abs(currentCount) >= Math.abs(targetCount); // TODO: Test
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

    public void setDrivePowers(double power) {
        setDrivePowers(new double[]{power, power, power, power});
    }

    /**
     * Sets all drive motor powers to zero
     */
    private void stop() {
        setDrivePowers(0.);
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


    private boolean reached(int one, int two) {
        return Math.abs(one - two) < 50; // TODO: Convert to constant
    }

    public void holonomicMotorControl(int xTarget, int yTarget, int thetaTarget) {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);// TODO: Stall detection
        setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // Makes sure that the starting tick count is 0

        PID xControl = new PID(motorKp, motorKi, motorKd);
        PID yControl = new PID(motorKp, motorKi, motorKd);
        PID thetaControl = new PID(motorKp, motorKi, motorKd);
        TimeoutManager timeoutManager = new TimeoutManager(100_000_000);
        int timeOutThreshold = 3; // If the encoder does not change by at least this number of ticks, the motor is "stuck"
        currentPosition = new Pose(0, 0, 0);
        while (!(reached(xTarget, currentPosition.x) && reached(yTarget, currentPosition.y) && reached(thetaTarget, currentPosition.heading)) && (!timeoutManager.isExceeded())) {
            updateCurrentPose(); // TODO: For merlin, you can just update the target pose after this line.
            double xPower = xControl.calculate(xTarget, currentPosition.x);
            double yPower = yControl.calculate(yTarget, currentPosition.y);
            double thetaPower = thetaControl.calculate(thetaTarget, currentPosition.heading);
            double x_rotated = xPower * Math.cos(thetaTarget) - yPower * Math.sin(thetaTarget);
            double y_rotated = xPower * Math.sin(thetaTarget) + yPower * Math.cos(thetaTarget);
            frontLeft.setPower(x_rotated + y_rotated + thetaPower);
            frontRight.setPower(x_rotated - y_rotated - thetaPower);
            rearLeft.setPower(x_rotated - y_rotated + thetaPower);
            rearRight.setPower(x_rotated + y_rotated - thetaPower);
        }
    }

    private void updateCurrentPose() {
        if (odometryEnabled) {
            // https://gm0.org/en/latest/docs/software/concepts/odometry.html
            int odlTicks = odL.getCurrentPosition();
            int odbTicks = odB.getCurrentPosition();
            int odrTicks = odR.getCurrentPosition();

            int deltaOdlTicks = odlTicks - previousLeftOdometryTicks;
            int deltaOdbTicks = odbTicks - previousBackOdometryTicks;
            int deltaOdrTicks = odrTicks - previousRightOdometryTicks;

            double deltaTheta = (deltaOdlTicks - deltaOdrTicks) / (ODOMETRY_TRACKWIDTH);
            double deltaXC = (double) (deltaOdlTicks + deltaOdrTicks) / 2;
            double deltaPerpendicular = deltaOdbTicks - ODOMETRY_BACK_DISPLACEMENT * deltaTheta;

            double deltaX = deltaXC * Math.cos(currentPosition.heading) - deltaPerpendicular * Math.sin(currentPosition.heading);
            double deltaY = deltaXC * Math.sin(currentPosition.heading) + deltaPerpendicular * Math.cos(currentPosition.heading);

            currentPosition.heading += (int) deltaTheta;
            currentPosition.x += (int) deltaX;
            currentPosition.y += (int) deltaY;

            previousLeftOdometryTicks = odlTicks;
            previousBackOdometryTicks = odbTicks;
            previousRightOdometryTicks = odrTicks;

        } else {
            int flTicks = frontLeft.getCurrentPosition();
            int frTicks = frontRight.getCurrentPosition();
            int rlTicks = rearLeft.getCurrentPosition();
            int rrTicks = rearRight.getCurrentPosition();
            throw new RuntimeException("Not yet implemented"); // TODO: Dead reckoning
        }
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


    /**
     * PID motor control program to ensure all four motors are synchronized
     *
     * @param tickCount How far each motor should go
     */
    public void allMotorControl(int[] tickCount, MoveSystem[] moveSystems) {
        logger.info("Moving " + Arrays.toString(tickCount));
        WebThread.addAction(new WebAction("drive", "Moving " + Arrays.toString(tickCount)));
        // Refresh motors
        stop();
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // Makes sure that the starting tick count is 0 TODO: Profile time for one cycle
        // Timeout controls (stop loop if motor stalls)
        TimeoutManager timeoutManager = new TimeoutManager(100_000_000);
        int timeOutThreshold = 3; // If the encoder does not change by at least this number of ticks, the motor is "stuck"

        // Initialize motor data wrappers
        MotorControlData fl = new MotorControlData(frontLeft, moveSystems[0], tickCount[0], timeOutThreshold); // TODO: Odometry implementation
        MotorControlData fr = new MotorControlData(frontRight, moveSystems[1], tickCount[1], timeOutThreshold);
        MotorControlData rl = new MotorControlData(rearLeft, moveSystems[2], tickCount[2], timeOutThreshold);
        MotorControlData rr = new MotorControlData(rearRight, moveSystems[3], tickCount[3], timeOutThreshold);
        // TODO: Profile init time
        while (((!fl.isDone) || (!fr.isDone) || (!rl.isDone) || (!rr.isDone)) && (!timeoutManager.isExceeded())) { // TODO: Profile time for one while loop cycle
//            WebThread.setPercentage("drive", fr.currentCount, fr.targetCount);
            // Update current variables
            // if only this got fixed ... then I could simplify the code even more
            fr.currentCount = (int) (fr.motor.getCurrentPosition() / 0.7); // FR is always off, not sure why TODO: Check again ...

            // Run a cycle for each
            fl.cycle(false);
            fr.cycle(true);
            rl.cycle(false);
            rr.cycle(false);
            if (fl.isNotMoving && fr.isNotMoving && rl.isNotMoving && rr.isNotMoving) {
                if (!timeoutManager.isStarted) {
                    timeoutManager.start();
                }
            } else {
                timeoutManager.stop();
            }
            if (debug) {
                logger.verbose("Target tick: " + fl.targetCount + " " + fr.targetCount + " " + rl.targetCount + " " + rr.targetCount);
                logger.verbose("Current tick: " + fl.currentCount + " " + fr.currentCount + " " + rl.currentCount + " " + rr.currentCount);
                logger.verbose("Current power: " + fl.motor.getPower() + " " + fr.motor.getPower() + " " + rl.motor.getPower() + " " + rr.motor.getPower()); // TODO: Profile for performance hit
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
        WebThread.position = new Vector(WebThread.position.add(v).toArray()); // TODO: Fix because angle isn't constant
        WebThread.theta += turnAngle;
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
        MoveSystem[] pids = {new PID(motorKp, motorKi, motorKd), new PID(motorKp, motorKi, motorKd), new PID(motorKp, motorKi, motorKd), new PID(motorKp, motorKi, motorKd)};
        allMotorControl(tickCount, pids);
        stop();
    }

    static class MotorControlData {
        DcMotorEx motor;
        MoveSystem moveSystem;
        boolean isNotMoving;
        boolean isDone;
        int currentCount;
        int prevCount;
        int targetCount;
        int timeOutThreshold;

        public MotorControlData(DcMotorEx motorEx, MoveSystem mS, int targetTickCount, int timeOutThreshold) {
            motor = motorEx;
            moveSystem = mS;
            isNotMoving = false;
            isDone = false;
            prevCount = -1;
            targetCount = targetTickCount;
            this.timeOutThreshold = timeOutThreshold;
        }

        public void updateCurrentCount() {
            currentCount = motor.getCurrentPosition();
        }

        public void setPower() {
            motor.setPower(DRIVE_SPEED * moveSystem.calculate(targetCount, currentCount));
        }

        public void halt() {
            isDone = true;
            isNotMoving = true;
            motor.setPower(0.0);
        }

        public void updateIsNotMoving() {
            if (prevCount != -1)
                isNotMoving = Math.abs(currentCount - prevCount) < timeOutThreshold;
        }

        public void updatePrevCount() {
            prevCount = currentCount;
        }

        public void cycle(boolean fRbypass) {
            if (!fRbypass)
                updateCurrentCount(); // House of cards moment
            setPower();
            checkMotorDone();
            updateIsNotMoving();
            updatePrevCount();
        }

        public void checkMotorDone() {
            if (isMotorDone(currentCount, targetCount)) {
                halt();
            }
        }
    }
}
