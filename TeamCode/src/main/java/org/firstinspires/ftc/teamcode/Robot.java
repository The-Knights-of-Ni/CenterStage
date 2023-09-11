package org.firstinspires.ftc.teamcode;

import android.util.Log;
import androidx.annotation.NonNull;
import com.qualcomm.hardware.lynx.LynxModule;
import android.os.Build;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Control.Control;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Drive;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.Vision;
import org.firstinspires.ftc.teamcode.Subsystems.Web.WebThread;
import org.firstinspires.ftc.teamcode.Subsystems.Web.WebThreadData;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.WebLog;

import java.util.HashMap;
import java.util.List;

public class Robot {
    public static final String name = "PowerPlay 2023";
    public final String initLogTag = "init";
    public static final double length = 18.0;
    public static final double width = 18.0;

    public final ElapsedTime timer;
    // DC Motors
    public DcMotorEx frontLeftDriveMotor;
    public DcMotorEx frontRightDriveMotor;
    public DcMotorEx rearRightDriveMotor;
    public DcMotorEx rearLeftDriveMotor;
    //Servos
    // Odometry
//    public List<LynxModule> allHubs;
//    public DigitalChannel odometryRA;
//    public DigitalChannel odometryRB;
//    public DigitalChannel odometryBA;
//    public DigitalChannel odometryBB;
//    public DigitalChannel odometryLA;
//    public DigitalChannel odometryLB;
    public int odRCount = 0;
    public int odBCount = 0;
    public int odLCount = 0;
    // Subsystems
    public Drive drive;
    public Control control;
    public Vision vision;
    public WebThread web;
    private final AllianceColor allianceColor;
    public final boolean visionEnabled;
    private final boolean webEnabled;
    private final boolean odometryEnabled;

    private WebThreadData wtd;
    private final HardwareMap hardwareMap;
    private final Telemetry telemetry;
    public GamepadWrapper gamepad1;
    public GamepadWrapper gamepad2;

    /**
     * @param timer         The elapsed time
     * @param allianceColor the alliance color
     */
    public Robot(@NonNull HardwareMap hardwareMap, @NonNull Telemetry telemetry, ElapsedTime timer,
                 AllianceColor allianceColor, Gamepad gamepad1, Gamepad gamepad2, HashMap<String, Boolean> flags) {
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.HTML); // Allow usage of some HTML tags
        telemetry.log().setDisplayOrder(Telemetry.Log.DisplayOrder.OLDEST_FIRST);
        telemetry.log().setCapacity(5);
        telemetryBroadcast("init", "started");
        Log.i(initLogTag, "started");
        Log.v(initLogTag, "android version: " + Build.VERSION.RELEASE);
//        double batteryVoltage = getBatteryVoltage();
//        if (batteryVoltage<11) {
//            Log.w(initLogTag, "Battery Voltage Low");
//            telemetry.addData("Warning", "<b>Battery Voltage Low!</b>");
//        }
        this.hardwareMap = hardwareMap;
        this.timer = timer;
        this.allianceColor = allianceColor;
        this.visionEnabled = flags.getOrDefault("vision", true);
        this.webEnabled = flags.getOrDefault("web", false);
        this.odometryEnabled = flags.getOrDefault("odometry", false);
        this.telemetry = telemetry;
        this.wtd = WebThreadData.getWtd();
        this.gamepad1 = new GamepadWrapper(gamepad1);
        this.gamepad2 = new GamepadWrapper(gamepad2);
        init();
    }


    public double getBatteryVoltage() {
        double result = Double.POSITIVE_INFINITY;
        for (VoltageSensor sensor: hardwareMap.voltageSensor) {
            double voltage = sensor.getVoltage();
            if (voltage > 0) {
                result = Math.min(result, voltage);
            }
        }
        return result;
    }

    public void init() {
        motorInit();
        servoInit();
        Log.i(initLogTag, "motor init finished");
        subsystemInit();
    }

    private void motorInit() {
        frontLeftDriveMotor = (DcMotorEx) hardwareMap.dcMotor.get("fl");
        frontRightDriveMotor = (DcMotorEx) hardwareMap.dcMotor.get("fr");
        rearLeftDriveMotor = (DcMotorEx) hardwareMap.dcMotor.get("rl");
        rearRightDriveMotor = (DcMotorEx) hardwareMap.dcMotor.get("rr");


        frontLeftDriveMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rearLeftDriveMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeftDriveMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontRightDriveMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rearLeftDriveMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rearRightDriveMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }

    private void servoInit() {
    }

    public void subsystemInit()
    {
        Log.d(initLogTag, "Drive subsystem init started");
        drive = new Drive(frontLeftDriveMotor, frontRightDriveMotor, rearLeftDriveMotor, rearRightDriveMotor, odometryEnabled, telemetry, timer);
        Log.i(initLogTag, "Drive subsystem init finished");

        Log.d(initLogTag, "Control subsystem init started");
        control = new Control(telemetry);
        Log.i(initLogTag, "Control subsystem init finished");

        if (visionEnabled) {
            Log.d("init", "Vision subsystem init started");
            vision = new Vision(telemetry, hardwareMap, allianceColor);
            Log.i("init", "Vision subsystem init finished");
        }
        else {
            Log.w(initLogTag, "Vision subsystem init skipped");
        }

        if (webEnabled) {
            try {
                Log.d("init", "Web subsystem init started");
                web = new WebThread(telemetry);
                web.run();
                wtd.addLog(new WebLog("init", "Started", WebLog.LogSeverity.INFO));
                Log.i("init", "Web subsystem init finished");
            }
            catch (Exception e) {
                Log.e(initLogTag, "Web Thread init failed " + e.getMessage());
            }
        }
        telemetryBroadcast("Status", "all subsystems initialized");
    }

    public Telemetry.Item telemetryBroadcast(String caption, String value) {
        Telemetry.Item resp = telemetry.addData(caption, value);
        telemetry.update();
        if (webEnabled)
            wtd.addLog(new WebLog(caption, value, WebLog.LogSeverity.INFO));
        Log.i(caption, value);
        return resp;
    }
    public void updateGamepads() {
        gamepad1.update();
        gamepad2.update();
    }
}
