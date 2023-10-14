package org.firstinspires.ftc.teamcode.Subsystems.Control;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Subsystem;


/**
 * Control subsystem for controlling arms and claws
 */
public class Control extends Subsystem {

    public static SlidePosition RETRACTED_SLIDE = new SlidePosition(0);
    public static SlidePosition SCORE_LOW_SLIDE = new SlidePosition(0);

    private Servo airplaneLauncher;

    public Control(Telemetry telemetry, Servo airplaneLauncher) {
        super(telemetry, "control");
        this.airplaneLauncher = airplaneLauncher;
    }

    public void initDevicesAuto() {
    }

    public void initDevicesTeleop() {
    }

    public void airplaneLaunch() throws InterruptedException {
        //launch plane
        airplaneLauncher.setPosition(1.0);
    }

    public void moveLinearSlide(SlidePosition pos) {
        //Move linear slide up and down
    }

    public void moveLinearSlideSync(SlidePosition pos) {
        //Move linear slide up and down
    }

    public void setLinearSlideMotorPower(double power) {
    }

    public void setClaw(ClawState clawState) {
    }

    public void setClawSync(ClawState clawState) {
    }

    public void openClaw() {
    }

    public void closeClaw() {
    }

    public void openClawSync() {
    }

    public void closeClawSync() {
    }

    public void moveCrane(CraneState craneState) {
    }

    public enum ClawState { //TODO: Calibrate claw constants
        OPEN(0),
        CLOSE(0);
        private final double position;

        ClawState(double position) {
            this.position = position;
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

    public static class SlidePosition { //TODO: Calibrate Slide Constants
        public double pos;

        SlidePosition(double pos) {
            this.pos = pos;
        }
    }
}
