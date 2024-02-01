package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Util.MasterLogger;

public class MotorControlData {
    DcMotorEx motor;
    PID moveSystem;
    boolean isNotMoving;
    boolean isDone;
    int currentCount;
    int prevCount;
    int targetCount;
    double power;
    int timeOutThreshold;
    MasterLogger motorLogger;
    private int noMovementTicks;
    static final int noMovementThreshold = 3;
    private boolean warned = false;

    public MotorControlData(DcMotorEx motorEx, PID mS, int targetTickCount, int timeOutThreshold, Telemetry telemetry, String name) {
        motor = motorEx;
        moveSystem = mS;
        isNotMoving = false;
        isDone = false;
        prevCount = -1;
        targetCount = targetTickCount;
        this.timeOutThreshold = timeOutThreshold;
        this.motorLogger = new MasterLogger(telemetry, name);
        this.noMovementTicks = 0;
    }

    public void updateCurrentCount() {
        currentCount = motor.getCurrentPosition();
    }

    /**
     * Sets the power of the motor
     * Do not use any other method to set the motor power, including {@link DcMotorEx#setPower(double)},
     * this will mess up the stall detection, as well as other things.
     *
     * <p>TODO: Benchmark {@link DcMotorEx#setPower(double)} to see if this is worth it</p>
     *
     * @param motorPower The power to set the motor to
     */
    public void setPower(double motorPower) {
        power = motorPower;
        motor.setPower(power);
    }

    public void setPower() {
        setPower(OldDrive.DRIVE_SPEED * moveSystem.calculate(targetCount, currentCount));
    }

    public void halt() {
        isDone = true;
        setPower(0.0);
        isNotMoving = true;
    }

    public void updateIsNotMoving() {
        if (prevCount != -1)
            isNotMoving = Math.abs(currentCount - prevCount) < timeOutThreshold;
    }

    public void updatePrevCount() {
        prevCount = currentCount;
    }

    public void cycle() {
        if (isDone)
            return;

        updateCurrentCount();
        setPower();
        checkMotorDone();
        if (isDone)
            return;
        updateIsNotMoving();
        if (Math.abs(currentCount - prevCount) < 5 && Math.abs(power) > 0.01) {
            this.noMovementTicks += 1;
        } else {
            this.noMovementTicks = 0;
        }
        if (this.noMovementTicks > noMovementThreshold && !warned) {
            this.motorLogger.warning("Motor is not moving");
            if (currentCount < 5 && power > 0.05) {
                this.motorLogger.warning("Motor Encoder is likely broken");
            }
            warned = true;
        }
        updatePrevCount();
    }

    public void checkMotorDone() {
        if (OldDrive.isMotorDone(currentCount, targetCount)) {
            isDone = true;
            halt();
        }
    }
}
