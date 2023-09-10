package org.firstinspires.ftc.teamcode.Subsystems.Control;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Subsystems.Subsystem;


/**
 * Control subsystem for controlling arms and claws
 */
public class Control extends Subsystem {

    private DcMotorEx bar;
    private Servo claw;
    private Servo clawAngle;
    private Servo arm;

    public enum BarState {
        HIGH(5700, 1.0), //TODO: calibrate constants
        MIDDLE(4810, 1.0),
        LOW(3575, 1.0),
        PICKUP(0, 1.0);

        public final int position;
        public final double power;

        BarState(int position, double power) {
            this.position = position;
            this.power = power;
        }
    }

    public enum ClawState {
        CLOSED(0.72),
        OPEN(0.65);

        public final double position;

        ClawState(double position) {
            this.position = position;
        }
    }

    public enum ArmState {
        DROPOFF(0.2),
        PICKUP(0);

        public final double position;

        ArmState(double position) {
            this.position = position;
        }
    }

    public enum ClawAngleState {
        PICKUP(0.05),
        DROPOFF(0.675);

        public final double position;

        ClawAngleState(double position) {
            this.position = position;
        }
    }

    public Control(Telemetry telemetry, DcMotorEx bar, Servo claw, Servo clawAngle, Servo arm) {
        super(telemetry, "control");
        this.bar = bar;
        this.claw = claw;
        this.clawAngle = clawAngle;
        this.arm = arm;
    }

    public void initDevicesAuto() {
        this.extendBar(BarState.PICKUP);
        this.toggleArm(ArmState.PICKUP);
        this.toggleClaw(ClawState.CLOSED);
        this.toggleClawAngle(ClawAngleState.PICKUP);

    }

    public void initDevicesTeleop() {
        this.extendBar(BarState.PICKUP);
        this.toggleArm(ArmState.PICKUP);
        this.toggleClaw(ClawState.CLOSED);
        this.toggleClawAngle(ClawAngleState.PICKUP);
    }

    public void extendBar(BarState position) {
        this.bar.setPower(position.power);
        this.bar.setTargetPosition(position.position);
        this.bar.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
    }

    public void toggleClaw(ClawState clawState) {
        this.claw.setPosition(clawState.position);
    }

    public void toggleArm(ArmState armState) {
        this.arm.setPosition(armState.position);
    }

    public void toggleClawAngle(ClawAngleState clawAngleState) {
        this.clawAngle.setPosition(clawAngleState.position);
    }

    class ScoreThread extends Thread {
        private final Control control;
        private final BarState barState;

        public ScoreThread(Control control, BarState barState) {
            this.control = control;
            this.barState = barState;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                if(barState == BarState.HIGH) {
                    control.toggleClawAngle(ClawAngleState.DROPOFF);
                    control.toggleArm(ArmState.DROPOFF);
                } else {
                    control.toggleClawAngle(ClawAngleState.PICKUP);
                    control.toggleArm(ArmState.PICKUP);
                }
            } catch (Exception e) {
//            telemetry.addData("Thread Status", "Interrupted " + e);
            }
        }
    }
    public void deploy(BarState barState) {
        this.extendBar(barState);
        new ScoreThread(this, barState).start();
    }
    public void dropoffArm() {
        this.toggleClawAngle(ClawAngleState.DROPOFF);
        this.toggleArm(ArmState.DROPOFF);
    }
    public void pickupArm() {
        this.toggleClawAngle(ClawAngleState.PICKUP);
        this.toggleArm(ArmState.PICKUP);
    }
    public void retract() {
        this.toggleClawAngle(ClawAngleState.PICKUP);
        this.toggleArm(ArmState.PICKUP);
        this.extendBar(BarState.PICKUP);
    }
}
