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
    public static final double length = 18;
    public static final double width = 18;

    public final ElapsedTime timer;
    // DC Motors
    public DcMotorEx frontLeftDriveMotor;
    public DcMotorEx frontRightDriveMotor;
    public DcMotorEx rearRightDriveMotor;
    public DcMotorEx rearLeftDriveMotor;
    public DcMotorEx bar;
    //Servos
    public Servo claw;
    public Servo clawAngle;
    public Servo arm;
    // Odometry
    public List<LynxModule> allHubs;
    public DigitalChannel odometryRA;
    public DigitalChannel odometryRB;
    public DigitalChannel odometryBA;
    public DigitalChannel odometryBB;
    public DigitalChannel odometryLA;
    public DigitalChannel odometryLB;
    public int odRCount = 0;
    public int odBCount = 0;
    public int odLCount = 0;
    /**
     * Declare game pad objects
     */
    public double leftStickX;
    public double leftStickY;
    public double rightStickX;
    public double rightStickY;
    public float triggerLeft;
    public float triggerRight;
    public boolean aButton = false;
    public boolean bButton = false;
    public boolean xButton = false;
    public boolean yButton = false;
    public boolean dPadUp = false;
    public boolean dPadDown = false;
    public boolean dPadLeft = false;
    public boolean dPadRight = false;
    public boolean bumperLeft = false;
    public boolean bumperRight = false;
    public double leftStickX2;
    public double leftStickY2;
    public double rightStickX2;
    public double rightStickY2;
    public float triggerLeft2;
    public float triggerRight2;
    public boolean aButton2 = false;
    public boolean bButton2 = false;
    public boolean xButton2 = false;
    public boolean yButton2 = false;
    public boolean dPadUp2 = false;
    public boolean dPadDown2 = false;
    public boolean dPadLeft2 = false;
    public boolean dPadRight2 = false;
    public boolean bumperLeft2 = false;
    public boolean bumperRight2 = false;
    public boolean isaButtonPressedPrev = false;
    public boolean isbButtonPressedPrev = false;
    public boolean isxButtonPressedPrev = false;
    public boolean isyButtonPressedPrev = false;
    public boolean isdPadUpPressedPrev = false;
    public boolean isdPadDownPressedPrev = false;
    public boolean isdPadLeftPressedPrev = false;
    public boolean isdPadRightPressedPrev = false;
    public boolean islBumperPressedPrev = false;
    public boolean isrBumperPressedPrev = false;
    public boolean isaButton2PressedPrev = false;
    public boolean isbButton2PressedPrev = false;
    public boolean isxButton2PressedPrev = false;
    public boolean isyButton2PressedPrev = false;
    public boolean isdPadUp2PressedPrev = false;
    public boolean isdPadDown2PressedPrev = false;
    public boolean isdPadLeft2PressedPrev = false;
    public boolean isdPadRight2PressedPrev = false;
    public boolean islBumper2PressedPrev = false;
    public boolean isrBumper2PressedPrev = false;
    // Subsystems
    public Drive drive;
    public Control control;
    public Vision vision;
    public WebThread web;
    private final AllianceColor allianceColor;
    public final boolean visionEnabled;
    private final boolean webEnabled;
    private final boolean visionCorrectionEnabled;
    private final boolean odometryEnabled;

    private WebThreadData wtd;
    private final HardwareMap hardwareMap;
    private final Telemetry telemetry;
    private static final double joystickDeadZone = 0.1;
    private final Gamepad gamepad1;
    private final Gamepad gamepad2;

    /**
     * @param timer         The elapsed time
     * @param allianceColor the alliance color
     */
    public Robot(@NonNull HardwareMap hardwareMap, @NonNull Telemetry telemetry, ElapsedTime timer,
                 AllianceColor allianceColor, Gamepad gamepad1, Gamepad gamepad2, HashMap<String, Boolean> flags) {        telemetry.setDisplayFormat(Telemetry.DisplayFormat.HTML); // Allow usage of some HTML tags
        telemetry.log().setDisplayOrder(Telemetry.Log.DisplayOrder.OLDEST_FIRST); // We show the log in oldest-to-newest order
        telemetry.log().setCapacity(5); // We can control the number of lines shown in the log
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
        this.visionCorrectionEnabled = flags.getOrDefault("visionCorrection", false);
        this.odometryEnabled = flags.getOrDefault("odometry", false);
        this.telemetry = telemetry;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.wtd = WebThreadData.getWtd();
        init();
    }


    public double getBatteryVoltage() {
        double result = Double.POSITIVE_INFINITY;
        for (VoltageSensor sensor : hardwareMap.voltageSensor) {
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

        bar = (DcMotorEx) hardwareMap.dcMotor.get("bar");
        bar.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        bar.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        bar.setPower(0.0);
    }

    private void servoInit() {
        claw = hardwareMap.servo.get("claw");
        clawAngle = hardwareMap.servo.get("clawAngle");
        arm = hardwareMap.servo.get("arm");
    }

    public void subsystemInit()
    {
        Log.d(initLogTag, "Drive subsystem init started");
        drive = new Drive(frontLeftDriveMotor, frontRightDriveMotor, rearLeftDriveMotor, rearRightDriveMotor, telemetry, timer);
        Log.i(initLogTag, "Drive subsystem init finished");

        Log.d(initLogTag, "Control subsystem init started");
        control = new Control(telemetry, bar, claw, clawAngle, arm);
        Log.i(initLogTag, "Control subsystem init finished");

        if (visionEnabled) {
            Log.d("init", "Vision subsystem init started");
            vision = new Vision(telemetry, hardwareMap, allianceColor, visionCorrectionEnabled);
            Log.i("init", "Vision subsystem init finished");
        }
        else {
            Log.w(initLogTag, "Vision subsystem init skipped");
        }
        telemetryBroadcast("Status", " all subsystems initialized");
    }

    public void getGamePadInputs() {
        isaButtonPressedPrev = aButton;
        isbButtonPressedPrev = bButton;
        isxButtonPressedPrev = xButton;
        isyButtonPressedPrev = yButton;
        isdPadUpPressedPrev = dPadUp;
        isdPadDownPressedPrev = dPadDown;
        isdPadLeftPressedPrev = dPadLeft;
        isdPadRightPressedPrev = dPadRight;
        islBumperPressedPrev = bumperLeft;
        isrBumperPressedPrev = bumperRight;
        leftStickX = joystickDeadzoneCorrection(gamepad1.left_stick_x);
        leftStickY = joystickDeadzoneCorrection(-gamepad1.left_stick_y);
        rightStickX = joystickDeadzoneCorrection(gamepad1.right_stick_x);
        rightStickY = joystickDeadzoneCorrection(gamepad1.right_stick_y);
        triggerLeft = gamepad1.left_trigger;
        triggerRight = gamepad1.right_trigger;
        aButton = gamepad1.a;
        bButton = gamepad1.b;
        xButton = gamepad1.x;
        yButton = gamepad1.y;
        dPadUp = gamepad1.dpad_up;
        dPadDown = gamepad1.dpad_down;
        dPadLeft = gamepad1.dpad_left;
        dPadRight = gamepad1.dpad_right;
        bumperLeft = gamepad1.left_bumper;
        bumperRight = gamepad1.right_bumper;

        isaButton2PressedPrev = aButton2;
        isbButton2PressedPrev = bButton2;
        isxButton2PressedPrev = xButton2;
        isyButton2PressedPrev = yButton2;
        isdPadUp2PressedPrev = dPadUp2;
        isdPadDown2PressedPrev = dPadDown2;
        isdPadLeft2PressedPrev = dPadLeft2;
        isdPadRight2PressedPrev = dPadRight2;
        islBumper2PressedPrev = bumperLeft2;
        isrBumper2PressedPrev = bumperRight2;
        leftStickX2 = joystickDeadzoneCorrection(gamepad2.left_stick_x);
        leftStickY2 = joystickDeadzoneCorrection(-gamepad2.left_stick_y);
        rightStickX2 = joystickDeadzoneCorrection(gamepad2.right_stick_x);
        rightStickY2 = joystickDeadzoneCorrection(-gamepad2.right_stick_y);
        triggerLeft2 = gamepad2.left_trigger;
        triggerRight2 = gamepad2.right_trigger;
        aButton2 = gamepad2.a;
        bButton2 = gamepad2.b;
        xButton2 = gamepad2.x;
        yButton2 = gamepad2.y;
        dPadUp2 = gamepad2.dpad_up;
        dPadDown2 = gamepad2.dpad_down;
        dPadLeft2 = gamepad2.dpad_left;
        dPadRight2 = gamepad2.dpad_right;
        bumperLeft2 = gamepad2.left_bumper;
        bumperRight2 = gamepad2.right_bumper;
    }

    /**
     * Discards joystick inputs between -joystickDeadZone and joystickDeadZone
     * @param joystickInput the input of the joystick
     * @return the corrected input of the joystick
     */
    public double joystickDeadzoneCorrection(double joystickInput) {
        double joystickOutput;
        if (joystickInput > joystickDeadZone) {
            joystickOutput = (joystickInput - joystickDeadZone) / (1.0 - joystickDeadZone);
        } else if (joystickInput > -joystickDeadZone) { // joystickInput is between -joystickDeadZone and joystickDeadZone
            joystickOutput = 0.0;
        } else { // if joystickInput is less than -joystickDeadZone
            joystickOutput = (joystickInput + joystickDeadZone) / (1.0 - joystickDeadZone);
        }
        return joystickOutput;
    }

    public void telemetryBroadcast(String caption, String value) {
        telemetry.addData(caption, value);
        telemetry.update();
        if (webEnabled)
            wtd.addLog(new WebLog(caption, value, WebLog.LogSeverity.INFO));
        Log.i(caption, value);
    }
}
