package org.firstinspires.ftc.teamcode.Subsystems.Control;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Subsystem;


/**
 * Control subsystem for controlling arms and claws
 */
public class Control extends Subsystem {

    public static SlidePosition RETRACTED_SLIDE = SlidePosition.DOWN;
    public static SlidePosition SCORE_LOW_SLIDE = SlidePosition.UP;

    private DcMotorEx slideMotor;
    private DcMotorEx intakeMotor;
    private DcMotorEx craneMotor;
    private Servo airplaneLauncher;
    private Servo airplaneLaunchAngle;
    private Servo clawOpenClose;
    private Servo clawShoulder;



    public Control(Telemetry telemetry, Servo airplaneLauncher, Servo airplaneLaunchAngle, Servo clawOpenClose, Servo clawShoulder, DcMotorEx slideMotor, DcMotorEx intakeMotor, DcMotorEx craneMotor) {
        super(telemetry, "control");
        this.airplaneLauncher = airplaneLauncher;
        this.airplaneLaunchAngle = airplaneLaunchAngle;
        this.clawOpenClose = clawOpenClose;
        this.clawShoulder = clawShoulder;
        this.slideMotor = slideMotor;
        this.intakeMotor = intakeMotor;
        this.craneMotor = craneMotor;
    }

    public void initDevices() {
        clawShoulder.setDirection(Servo.Direction.REVERSE);

        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        airplaneLauncher.setDirection(Servo.Direction.REVERSE);
        airplaneLaunchAngle.setDirection(Servo.Direction.REVERSE);
        clawOpenClose.setDirection(Servo.Direction.FORWARD);
        clawShoulder.setDirection(Servo.Direction.REVERSE);
    }

    public void airplaneLaunch() throws InterruptedException {
        //launch plane
        airplaneLauncher.setPosition(1.0);
    }
    public void setAirplaneAngle() {
        airplaneLaunchAngle.setPosition(0.870);
    }

    public void resetAirplaneAngle() {
        airplaneLaunchAngle.setPosition(0);
    }

    public void moveLinearSlide(SlidePosition pos) {
        slideMotor.setTargetPosition(pos.pos);
    }

    public void moveLinearSlideSync(SlidePosition pos) {
        slideMotor.setTargetPosition(pos.pos);
    }

    public void setLinearSlideMotorPower(double power) {
        slideMotor.setPower(power);
    }
    public void setCraneMotorPower(double power) {
        craneMotor.setPower(power);
    }

    public void setClaw(ClawState clawState) {
        clawOpenClose.setPosition(clawState.clawPosition);
        clawShoulder.setPosition(clawState.shoulderPosition);
    }

    public void setClawSync(ClawState clawState) {
        clawOpenClose.setPosition(clawState.clawPosition);
        clawShoulder.setPosition(clawState.shoulderPosition);
    }

    public void runIntake() {
        intakeMotor.setPower(1);
    }

    public void stopIntake() {
        intakeMotor.setPower(0);
    }

    public void openClaw() {
        clawOpenClose.setPosition(1.0);
    }

    public void closeClaw() {
        clawOpenClose.setPosition(0);
    }

    public void extendShoulder() {
        clawShoulder.setPosition(0.66); // Do not change constant, trial and error somehow worked
    }

    public void pickupPosShoulder() {
        clawShoulder.setPosition(0.612);
    }

    public void retractShoulder() {
        clawShoulder.setPosition(0.6);
    }

    public void openClawSync() {
        clawOpenClose.setPosition(1);
    }

    public void closeClawSync() {
        clawOpenClose.setPosition(0);
    }

    public void moveCrane(CraneState craneState) {
        // TBD
    }

    public enum ClawState { //TODO: Calibrate claw constants
        SCORE(1,1),
        RETRACT(0,0);
        private final double clawPosition;
        private final double shoulderPosition;

        ClawState(double clawPosition, double shoulderPosition) {
            this.clawPosition = clawPosition;
            this.shoulderPosition = shoulderPosition;
        }
    }

    public enum CraneState { //TODO: Calibrate crane constants
        UP(0),
        DOWN(0);
        public double tickCount;

        CraneState(double tickCount) {
            this.tickCount = tickCount;
        }
    }

    public enum SlidePosition { //TODO: Calibrate Slide Constants
        UP(1),
        DOWN(0);
        public int pos;

        SlidePosition(int pos) {
            this.pos = pos;
        }
    }
}
