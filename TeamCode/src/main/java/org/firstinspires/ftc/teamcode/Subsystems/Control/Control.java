package org.firstinspires.ftc.teamcode.Subsystems.Control;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Subsystem;


/**
 * Control subsystem for controlling arms and claws
 */
public class Control extends Subsystem {

    public static SlidePosition RETRACTED_SLIDE = new SlidePosition(0);
    public static SlidePosition SCORE_LOW_SLIDE = new SlidePosition(0);

    private DcMotorEx airplaneLauncher;

    public Control(Telemetry telemetry, DcMotorEx airplaneLauncher) {
        super(telemetry, "control");
        this.airplaneLauncher = airplaneLauncher;
    }

    public void initDevicesAuto() {
    }

    public void initDevicesTeleop() {
    }

    public void airplaneLaunch(PlaneLaunchRange range) throws InterruptedException {
        //launch plane
        airplaneLauncher.setPower(range.motorPower);
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

    public enum PlaneLaunchRange { //TODO: Calibrate Motor Powers
        LONG(1.0),
        MEDIUM(0.7),
        SHORT(0.3),
        OFF(0);

        private final double motorPower;

        PlaneLaunchRange(double motorPower) {
            this.motorPower = motorPower;
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
