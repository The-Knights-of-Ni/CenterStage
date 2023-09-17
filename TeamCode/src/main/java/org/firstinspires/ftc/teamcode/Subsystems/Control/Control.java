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

    public enum DoubleGrippyState { //TODO: Calibrate claw constants
        CLOSE_FRONT(0,0),
        CLOSE_REAR(0,0),
        OPEN_FRONT(0,0),
        OPEN_REAR(0,0),
        OPEN_SIMUL(0,0),
        CLOSE_SIMUL(0,0);
        private double posFront;
        private double posRear;
        private double pivotAngle;
        DoubleGrippyState(double posFront, double posRear) {
            this.posFront = posFront;
            this.posRear = posRear;
        }
    }

    public enum PlaneLaunchRange { //TODO: Calibrate Motor Powers
        LONG(0),
        MEDIUM(0),
        SHORT(0);
        private double motorPower;
        PlaneLaunchRange(double motorPower) {
            this.motorPower = motorPower;
        }
    }

    public enum SlidePosition { //TODO: Calibrate Slide Constants
        EXTENDED(0),
        RETRACTED(0),
        OBJ_PICKUP(0);
        private double pos;
        SlidePosition(double pos) {
            this.pos = pos;
        }
    }

    public enum CraneState { //TODO: Calibrate crane constants
        UP(0),
        DOWN(0);
        private double tickCount;
        CraneState(double tickCount) {
            this.tickCount = tickCount;
        }
    }

    public void initDevicesAuto() {

    }

    public void initDevicesTeleop() {

    }

    public void intakePixel() {

    }

    public void pivotDoubleGrippy(double pos) {
        //Pivot the double grippy
    }

    public void moveDoubleGrippy(DoubleGrippyState pos) {
        //move claw thing
    }

    public void craneLift(CraneState state) {
        //Crane operation
    }

    public void airplaneLaunch(PlaneLaunchRange range) {
        //launch plane
    }

    public void moveLinearSlide(SlidePosition pos) {
        //Move linear slide up and down
    }

}
