package org.firstinspires.ftc.teamcode.Subsystems.Control;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotorControlData;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.PID;
import org.firstinspires.ftc.teamcode.Util.MasterLogger;

import java.util.Locale;

/**
 * Turns any motor into a precise targeter, kind of like a servo.
 * This is useful for control, when {@link DcMotorEx.RunMode#RUN_TO_POSITION} is not viable.
 * @param <I> The constants for the linear slide {@link TargetedMotorConstants}
 */
public class TargetedMotor<I extends TargetedMotorConstants> {
    DcMotorEx innerMotor;
    TargetedMotorConstants constants;
    PID pid;
    MasterLogger logger;
    ElapsedTime timer = new ElapsedTime();
    boolean debug = false;

    /**
     * Create a new linear slide
     * @param innerMotor The motor that controls the linear slide
     * @param constants The constants for the linear slide
     */
    public TargetedMotor(DcMotorEx innerMotor, I constants, PID pid, Telemetry telemetry) {
        this.innerMotor = innerMotor;
        this.constants = constants;
        this.pid = pid;
        this.logger = new MasterLogger(telemetry, "LinearSlide");
        innerMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void move(int pos) {
        // Timeout control (stop loop if motor stalls)
        long currentTime; // current time in nanoseconds
        long startTime = timer.nanoseconds();
        boolean isTimeOutStarted = false;
        boolean isTimeOutExceeded = false; // If timeout is exceeded pid stops and logs an error
        int timeOutPeriod = 100_000_000;
        double timeOutStartedTime = 0.0;
        // If the encoder does not change by at least this number of ticks, the motor is considered to be stuck
        int timeOutThreshold = 3;
        MotorControlData motorControlData = new MotorControlData(innerMotor, pid, pos, timeOutThreshold, logger.telemetry, "linearSlide");
        while (((!motorControlData.isDone) && (!isTimeOutExceeded))) {
            // Update current variables
            currentTime = timer.nanoseconds() - startTime;

            // Run a cycle for each
            motorControlData.cycle();
            if (motorControlData.isNotMoving) {
                if (isTimeOutStarted && currentTime - timeOutStartedTime > timeOutPeriod) {
                    isTimeOutExceeded = true;
                    logger.info("Move failed, timeout exceeded");
                } else { // time out was not started yet
                    isTimeOutStarted = true;
                    timeOutStartedTime = currentTime;
                }
            } else {
                isTimeOutStarted = false;
            }
            if (debug) {
                logger.verbose(Locale.US, "Current Tick Count Check: %d", innerMotor.getCurrentPosition());
                logger.verbose(Locale.US, "PID Dump: " + motorControlData.moveSystem.toString());
                logger.debug(Locale.US, "Motor Info: %.2f;%d/%d", motorControlData.power, motorControlData.currentCount, motorControlData.targetCount);
            }
        }
    }

    public void move(String name) {
        move(constants.get(name));
    }
}
