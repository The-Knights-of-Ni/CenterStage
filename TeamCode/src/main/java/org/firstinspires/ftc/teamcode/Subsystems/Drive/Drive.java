package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import androidx.annotation.Nullable;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.*;
import org.firstinspires.ftc.teamcode.Geometry.Path;
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
    public static double COUNTS_PER_DEGREE = 1180 / 90; // 1000 ticks per 90 degrees

    // Default drive speeds
    public static double DRIVE_SPEED = 0.60;
    // Move PID coefficients
    public static PIDCoefficients xyPIDCoefficients = new PIDCoefficients(0.0025, 0.000175, 0.0003); // TODO: calibrate
    public static PIDCoefficients thetaPIDCoefficients = new PIDCoefficients(0.00010, 0.000500, 0.00015); // TODO: calibrate
    // Drive-train motors
    public final MotorGeneric<DcMotorEx> motors;
    // Odometry Encoders/Constants
    public boolean odometryEnabled;
    private final DcMotorEx odL;
    private final DcMotorEx odB;
    private final DcMotorEx odR;
    public double ODOMETRY_TRACKWIDTH = 10.0; // TODO: Calibrate
    public double ODOMETRY_BACK_DISPLACEMENT = 10.0; // How far back the back odometry wheel is TODO: Calibrate

    public double ODOMETRY_COUNTS_PER_MM = 3; // TODO: Calibrate


    public boolean debug = false;

    private BNO055IMU imu;
    public static Pose currentPosition = new Pose(0, 0, 0);
    public int previousLeftOdometryTicks = 0;
    public int previousBackOdometryTicks = 0;
    public int previousRightOdometryTicks = 0;


    /**
     * Initializes the drive subsystem
     *
     * @param motors      The motors ...
     * @param telemetry   The telemetry
     * @param elapsedTime The timer for the elapsed time
     */
    public Drive(MotorGeneric<DcMotorEx> motors, @Nullable DcMotorEx[] odometry, BNO055IMU imu, Telemetry telemetry, ElapsedTime elapsedTime) {
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
        this.motors = motors;
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
        this.motors.frontLeft.setZeroPowerBehavior(mode);
        this.motors.frontRight.setZeroPowerBehavior(mode);
        this.motors.rearLeft.setZeroPowerBehavior(mode);
        this.motors.rearRight.setZeroPowerBehavior(mode);
    }

    /**
     * Uniformly sets run mode of all drive motors
     *
     * @param mode Run mode
     * @see DcMotorEx#setMode(DcMotor.RunMode)
     */
    public void setRunMode(DcMotor.RunMode mode) {
        this.motors.frontLeft.setMode(mode);
        this.motors.frontRight.setMode(mode);
        this.motors.rearLeft.setMode(mode);
        this.motors.rearRight.setMode(mode);
    }

    /**
     * Sets the drive power of each motor individually.
     *
     * @param powers the powers to set each of the motors to
     * @see DcMotorEx#setPower(double)
     */
    public void setDrivePowers(MotorGeneric<Double> powers) {
        this.motors.frontLeft.setPower(powers.frontLeft);
        this.motors.frontRight.setPower(powers.frontRight);
        this.motors.rearLeft.setPower(powers.rearLeft);
        this.motors.rearRight.setPower(powers.rearRight);
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
        double r = Math.hypot(leftStickX, leftStickY);
        double robotAngle = Math.atan2(leftStickY, leftStickX) - Math.PI / 4;
        double lrPower = r * Math.sin(robotAngle) + rightStickX;
        double lfPower = r * Math.cos(robotAngle) + rightStickX;
        double rrPower = r * Math.cos(robotAngle) - rightStickX;
        double rfPower = r * Math.sin(robotAngle) - rightStickX;
        return new MotorGeneric<>(lfPower, rfPower, lrPower, rrPower);
    }

    public void motorController(Targeter targeter, Controller controller) {
        imu.startAccelerationIntegration(new Position(DistanceUnit.MM, 0, 0, 0, 100), new Velocity(DistanceUnit.MM, 0, 0, 0, 500), 100);
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Makes sure that the starting tick count is 0 (just in case we're using dead reckoning, which relies on tick counts from the motor encoders) TODO: It's probably going to be relative tick counts, so idk why this is a thing here ...
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // TODO: Check if acquisition time is correct
        double imuHeadingStart = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle;
        double imuXStart = imu.getPosition().x;
        double imuYStart = imu.getPosition().y;
        // Timeout manager for when the robot gets stuck
        TimeoutManager timeoutManager = new TimeoutManager(100_000_000);
        int timeOutThreshold = 3; // If the encoder does not change by at least this number of ticks, the motor is "stuck"
        currentPosition = new Pose(0, 0, 0);
        MotorGeneric<Integer> previousTickCounts = new MotorGeneric<>(0, 0, 0, 0);
        MotorGeneric<Integer> currentTickCounts;
        while (!targeter.reachedTarget(currentPosition) && (!timeoutManager.isExceeded())) {
            // Approximates the current position (odometry or dead reckoning) it should be reasonably accurate
            updateCurrentPose(imuXStart, imuYStart, imuHeadingStart);
            // Feeds pose into targeter to get target ...
            Pose target = targeter.getTarget(currentPosition);
            logger.verbose("Current: " + currentPosition.toString());
            logger.debug("Target: " + target.toString());
            // Feeds target into controller to get motor powers
            MotorGeneric<Double> motorPowers = controller.calculate(target, currentPosition);
            // sets the motor powers
            setDrivePowers(motorPowers);
            // Checks if the robot is stuck
            currentTickCounts = new MotorGeneric<>(motors.frontLeft.getCurrentPosition(), motors.frontRight.getCurrentPosition(), motors.rearLeft.getCurrentPosition(), motors.rearRight.getCurrentPosition());
            if (Math.abs(currentTickCounts.frontLeft - previousTickCounts.frontLeft) > timeOutThreshold || Math.abs(currentTickCounts.frontRight - previousTickCounts.frontRight) > timeOutThreshold || Math.abs(currentTickCounts.rearLeft - previousTickCounts.rearLeft) > timeOutThreshold || Math.abs(currentTickCounts.rearRight - previousTickCounts.rearRight) > timeOutThreshold) {
                timeoutManager.start();
            } else {
                timeoutManager.stop();
            }
            previousTickCounts = currentTickCounts;
        }
        imu.stopAccelerationIntegration();
        stop(); // Stops the robot ... TODO: Maybe don't stop the robot if the target has a specified velocity?
    }

    private void updateCurrentPose(double imuXStart, double imuYStart, double imuHeadingStart) {
        if (odometryEnabled) {
            // https://gm0.org/en/latest/docs/software/concepts/odometry.html
            int odlTicks = odL.getCurrentPosition();
            int odbTicks = odB.getCurrentPosition();
            int odrTicks = odR.getCurrentPosition();

            int deltaOdlTicks = odlTicks - previousLeftOdometryTicks;
            int deltaOdbTicks = odbTicks - previousBackOdometryTicks;
            int deltaOdrTicks = odrTicks - previousRightOdometryTicks;

            double deltaOdlMM = deltaOdlTicks / ODOMETRY_COUNTS_PER_MM;
            double deltaOdbMM = deltaOdbTicks / ODOMETRY_COUNTS_PER_MM;
            double deltaOdrMM = deltaOdrTicks / ODOMETRY_COUNTS_PER_MM;

            double deltaTheta = (deltaOdlMM - deltaOdrMM) / (ODOMETRY_TRACKWIDTH);
            double deltaXC = (deltaOdlMM + deltaOdrMM) / 2;
            double deltaPerpendicular = deltaOdbMM - ODOMETRY_BACK_DISPLACEMENT * deltaTheta;

            double deltaX = deltaXC * Math.cos(currentPosition.heading) - deltaPerpendicular * Math.sin(currentPosition.heading);
            double deltaY = deltaXC * Math.sin(currentPosition.heading) + deltaPerpendicular * Math.cos(currentPosition.heading);

            currentPosition.heading += deltaTheta;
            currentPosition.x += deltaX;
            currentPosition.y += deltaY;
            Velocity velocity = imu.getVelocity().toUnit(DistanceUnit.MM);
            currentPosition.velocity = new Vector(velocity.xVeloc, velocity.yVeloc);

            previousLeftOdometryTicks = odlTicks;
            previousBackOdometryTicks = odbTicks;
            previousRightOdometryTicks = odrTicks;
        } else {
            Position position = imu.getPosition().toUnit(DistanceUnit.MM);
            currentPosition.x = position.y - imuYStart;
            currentPosition.y = position.x - imuXStart; // IMU inverts stuff
            currentPosition.heading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle - imuHeadingStart;
            Velocity velocity = imu.getVelocity().toUnit(DistanceUnit.MM);
            currentPosition.velocity = new Vector(velocity.xVeloc, velocity.yVeloc);
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

    private HolonomicController getHolonomicController() {
        return new HolonomicController(new PID(xyPIDCoefficients), new PID(xyPIDCoefficients), new PID(thetaPIDCoefficients));
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
}
