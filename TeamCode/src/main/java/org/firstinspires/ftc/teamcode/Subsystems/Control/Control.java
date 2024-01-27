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

    public final DcMotorEx slideMotor;
    public final DcMotorEx intakeMotor;
    public final DcMotorEx craneMotor;
    public final Servo airplaneLauncher;
    public final Servo airplaneLaunchAngle;
    public final Servo clawOpenClose;
    public final Servo clawShoulder;


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

    public void initDevicesAuto() {
        clawShoulder.setDirection(Servo.Direction.REVERSE);

        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideMotor.setTargetPosition(0);

        craneMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        airplaneLauncher.setDirection(Servo.Direction.REVERSE);
        airplaneLaunchAngle.setDirection(Servo.Direction.REVERSE);
        clawOpenClose.setDirection(Servo.Direction.FORWARD);
        clawShoulder.setDirection(Servo.Direction.REVERSE);
    }

    public void initDevicesTeleop() {
        clawShoulder.setDirection(Servo.Direction.REVERSE);

        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        craneMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        craneMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        airplaneLauncher.setDirection(Servo.Direction.REVERSE);
        airplaneLaunchAngle.setDirection(Servo.Direction.REVERSE);
        clawOpenClose.setDirection(Servo.Direction.FORWARD);
        clawShoulder.setDirection(Servo.Direction.REVERSE);
    }

    public void airplaneLaunch() {
        // launch plane
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
        moveLinearSlide(pos);
        while (slideMotor.isBusy()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Moves the crane in a safe way by also moving the slide.
     * The crane and slide are connected, so they must be moved together.
     */
    public void setCraneLinearSlideMotorPower(double power) {
        slideMotor.setPower(power); // TODO: This can go out of sync really quick, maybe use a PID ...
        craneMotor.setPower(power);
    }

    /**
     * @deprecated this could break stuff
     * @param power
     */
    @Deprecated
    public void setCraneMotorPower(double power) {
        craneMotor.setPower(power); // Inherently unsafe
    }

    public void runIntake() {
        intakeMotor.setPower(1);
    }

    public void stopIntake() {
        intakeMotor.setPower(0);
    }

    public void openClaw() {
        clawOpenClose.setPosition(0);
    }

    public void closeClaw() {
        clawOpenClose.setPosition(1);
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
        clawOpenClose.setPosition(0);
        while (Math.abs(clawOpenClose.getPosition() - 0) > 0.05) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void closeClawSync() {
        clawOpenClose.setPosition(1);
        while (Math.abs(clawOpenClose.getPosition() - 1) > 0.05) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public enum SlidePosition {
        UP(3010),
        DOWN(0);
        public final int pos;

        SlidePosition(int pos) {
            this.pos = pos;
        }
    }
}
