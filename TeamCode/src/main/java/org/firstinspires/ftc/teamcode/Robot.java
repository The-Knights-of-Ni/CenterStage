package org.firstinspires.ftc.teamcode;

import android.os.Build;
import android.util.Log;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Control.Control;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotorGeneric;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.OldDrive;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.Vision;
import org.firstinspires.ftc.teamcode.Subsystems.Web.WebLog;
import org.firstinspires.ftc.teamcode.Subsystems.Web.WebThread;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.BasicAccelerationIntegrator;
import org.firstinspires.ftc.teamcode.Util.MasterLogger;

import java.util.HashMap;

/**
 * Glue class for all subsystems
 * <p>All competition OpModes instantiate this class, as well as some Test OpModes.</p>
 * <p>This will initialize all subsystems, but certain can be disabled with flags ("vision", and "web")</p>
 */
public class Robot {
    public static final double length = 18.0;
    public static final double width = 18.0;
    private final MasterLogger logger;
    public static GamepadWrapper gamepad1;
    public static GamepadWrapper gamepad2;
    public final String initLogTag = "init";
    public final ElapsedTime timer;
    public final boolean visionEnabled;
    private final AllianceColor allianceColor;
    private final boolean webEnabled;
    private final boolean odometryEnabled;
    public final HardwareMap hardwareMap;
    private final Telemetry telemetry;

    public BNO055IMU imu;
    // Subsystems
    public OldDrive drive;
    public Control control;
    public Vision vision;
    public WebThread web;

    /**
     * @param hardwareMap   The hardware map for the robot
     * @param telemetry     The telemetry object
     * @param timer         The elapsed time
     * @param allianceColor the alliance color of the robot, usually set on a per-opmode basis
     * @param gamepad1      The first gamepad (the robot movement controller)
     * @param gamepad2      The second gamepad (control for the arms and claws)
     * @param flags          A hashmap of flags, used to disable certain subsystems
     * <p><b>Flags:</b></p>
     * <ul>
     *    <li><i>vision</i> - toggles vision subsystem, enabled by default</li>
     *    <li><i>web</i> - toggles web subsystem, disabled by default</li>
     *    <li><i>odometry</i> - toggles odometry subsystem, disabled by default</li>
     * </ul>
     */
    public Robot(HardwareMap hardwareMap, Telemetry telemetry, ElapsedTime timer,
                 AllianceColor allianceColor, Gamepad gamepad1, Gamepad gamepad2, HashMap<String, Boolean> flags) {
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.HTML); // Allow usage of some HTML tags
        telemetry.log().setDisplayOrder(Telemetry.Log.DisplayOrder.OLDEST_FIRST);
        telemetry.log().setCapacity(5);
        this.telemetry = telemetry;
        this.logger = new MasterLogger(telemetry, "Robot");
        logger.info("started");
        logger.verbose("android version: " + Build.VERSION.RELEASE);
        this.hardwareMap = hardwareMap;
        double batteryVoltage = getBatteryVoltage();
        if (batteryVoltage < 11) {
            Log.w(initLogTag, "Battery Voltage Low");
            telemetry.addData("Warning", "<b>Battery Voltage Low!</b>");
        }
        this.timer = timer;
        this.allianceColor = allianceColor;
        this.visionEnabled = flags.getOrDefault("vision", true);
        this.webEnabled = flags.getOrDefault("web", false);
        this.odometryEnabled = flags.getOrDefault("odometry", false);
        Robot.gamepad1 = new GamepadWrapper(gamepad1);
        Robot.gamepad2 = new GamepadWrapper(gamepad2);
        init();
    }

    public static void updateGamepads() {
        gamepad1.update();
        gamepad2.update();
    }

    public double getBatteryVoltage() {
        double result = Double.POSITIVE_INFINITY;
        if (hardwareMap.voltageSensor != null) {
            for (VoltageSensor sensor : hardwareMap.voltageSensor) {
                double voltage = sensor.getVoltage();
                if (voltage > 0) {
                    result = Math.min(result, voltage);
                }
            }
        }
        return result;
    }

    /**
     * Runs all init operations
     */
    protected void init() {
//        imuInit();
        logger.info("imu init finished");
        subsystemInit();
    }

    protected void imuInit() {
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile =
                "IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new BasicAccelerationIntegrator();
        parameters.temperatureUnit = BNO055IMU.TempUnit.FARENHEIT;

        telemetryBroadcast("Status", " IMU initializing...");
        imu.initialize(parameters);
        telemetryBroadcast("Status", " IMU calibrating...");
        // make sure the imu gyro is calibrated before continuing.
        while (!imu.isGyroCalibrated()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void subsystemInit() {
        logger.debug("Drive subsystem init started");
        var frontLeftDriveMotor = (DcMotorEx) hardwareMap.dcMotor.get("fl");
        var frontRightDriveMotor = (DcMotorEx) hardwareMap.dcMotor.get("fr");
        var rearLeftDriveMotor = (DcMotorEx) hardwareMap.dcMotor.get("rl");
        var rearRightDriveMotor = (DcMotorEx) hardwareMap.dcMotor.get("rr");
        if (odometryEnabled) {
            var leftEncoder = (DcMotorEx) hardwareMap.dcMotor.get("leftEncoder");
            var backEncoder = (DcMotorEx) hardwareMap.dcMotor.get("backEncoder");
            var rightEncoder = (DcMotorEx) hardwareMap.dcMotor.get("rightEncoder");
            drive = new OldDrive(new MotorGeneric<>(frontLeftDriveMotor, frontRightDriveMotor, rearLeftDriveMotor, rearRightDriveMotor), new DcMotorEx[]{leftEncoder, backEncoder, rightEncoder}, imu, telemetry, timer);
        } else {
            drive = new OldDrive(new MotorGeneric<>(frontLeftDriveMotor, frontRightDriveMotor, rearLeftDriveMotor, rearRightDriveMotor), null, imu, telemetry, timer);
        }
        logger.info("Drive subsystem init finished");

        logger.debug("Control subsystem init started");
        var slideMotor = (DcMotorEx) hardwareMap.dcMotor.get("slide");
        var intakeMotor = (DcMotorEx) hardwareMap.dcMotor.get("intake");
        var craneMotor = (DcMotorEx) hardwareMap.dcMotor.get("crane");
        var airplaneLauncher = hardwareMap.servo.get("plane");
        var airplaneLaunchAngle = hardwareMap.servo.get("planePivot");
        var clawOpenClose = hardwareMap.servo.get("claw");
        var clawShoulder = hardwareMap.servo.get("clawPivot");
        control = new Control(telemetry, airplaneLauncher, airplaneLaunchAngle, clawOpenClose, clawShoulder, slideMotor, intakeMotor, craneMotor);
        logger.info("Control subsystem init finished");

        if (visionEnabled) {
            logger.debug("Vision subsystem init started");
            vision = new Vision(telemetry, hardwareMap, allianceColor);
            logger.info("Vision subsystem init finished");
        } else {
            logger.warning("Vision subsystem init skipped");
        }

        if (webEnabled) {
            try {
                logger.debug("Web subsystem init started");
                web = new WebThread();
                web.start();
                WebThread.addLog(new WebLog("init", "web thread started", WebLog.LogSeverity.INFO));
                logger.info("Web subsystem init finished");
            } catch (Exception e) {
                logger.error("Web Thread init failed " + e.getMessage(), e);
            }
        } else {
            logger.warning("Web subsystem init skipped");
        }
        telemetryBroadcast("Status", "all subsystems initialized");
    }

    public void telemetryBroadcast(String caption, String value) {
        telemetry.addData(caption, value);
        telemetry.update();
        if (webEnabled) {
            WebThread.addLog(new WebLog(caption, value, WebLog.LogSeverity.INFO));
        }
        Log.i(caption, value);
    }


}
