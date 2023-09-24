package org.firstinspires.ftc.teamcode.Subsystems.Control;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Subsystem;


/**
 * Control subsystem for controlling arms and claws
 */
public class Control extends Subsystem {

    public Control(Telemetry telemetry) {
        super(telemetry, "control");
    }

    public enum ClawState { //TODO: Calibrate claw constants
        OPEN(0),
        CLOSE(0);
        private double position;

        ClawState(double position) {
            this.position = position;
        }
    }

    public enum PlaneLaunchRange { //TODO: Calibrate Motor Powers
        LONG(0),
        MEDIUM(0),
        SHORT(0),
        OFF(0);

        private double motorPower;

        PlaneLaunchRange(double motorPower) {
            this.motorPower = motorPower;
        }
    }

    public static class SlidePosition { //TODO: Calibrate Slide Constants
        public double pos;

        SlidePosition(double pos) {
            this.pos = pos;
        }
    }

    public static SlidePosition RETRACTED_SLIDE = new SlidePosition(0);
    public static SlidePosition SCORE_LOW_SLIDE = new SlidePosition(0);

    public enum CraneState { //TODO: Calibrate crane constants
        UP(0),
        DOWN(0);
        public double tickCount;

        CraneState(double tickCount) {
            this.tickCount = tickCount;
        }
    }

    public void initDevicesAuto() {

    }

    public void initDevicesTeleop() {

    }

    public void airplaneLaunch(PlaneLaunchRange range) {
        //launch plane
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
}
