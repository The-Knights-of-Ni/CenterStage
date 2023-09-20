package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class MockDcMotorEx implements DcMotorEx {
    RunMode currentRunMode;
    double currentPower;
    boolean motorEnabled;
    int currentPosition;
    Direction currentDirection;
    int targetPosition;
    ZeroPowerBehavior powerBehavior;
    ElapsedTime timer;

    public MockDcMotorEx(RunMode runMode) {
        timer = new ElapsedTime();
        currentRunMode = runMode;
        currentPower = 0;
        motorEnabled = true;
        currentDirection = Direction.FORWARD;
        targetPosition = 0;
        currentPosition = 0;
        powerBehavior = ZeroPowerBehavior.BRAKE;
    }

    public MockDcMotorEx() {
        this(RunMode.RUN_TO_POSITION);
    }

    @Override
    public void setMotorEnable() {
        motorEnabled = true;
    }

    @Override
    public void setMotorDisable() {
        motorEnabled = false;
    }

    @Override
    public boolean isMotorEnabled() {
        return motorEnabled;
    }

    @Override
    public void setVelocity(double angularRate) {

    }

    @Override
    public void setVelocity(double angularRate, AngleUnit unit) {

    }

    @Override
    public double getVelocity() {
        return 0;
    }

    @Override
    public double getVelocity(AngleUnit unit) {
        return 0;
    }

    @Override
    public void setPIDCoefficients(RunMode mode, PIDCoefficients pidCoefficients) {

    }

    @Override
    public void setPIDFCoefficients(RunMode mode, PIDFCoefficients pidfCoefficients) throws UnsupportedOperationException {

    }

    @Override
    public void setVelocityPIDFCoefficients(double p, double i, double d, double f) {

    }

    @Override
    public void setPositionPIDFCoefficients(double p) {

    }

    @Override
    public PIDCoefficients getPIDCoefficients(RunMode mode) {
        return null;
    }

    @Override
    public PIDFCoefficients getPIDFCoefficients(RunMode mode) {
        return null;
    }

    @Override
    public void setTargetPositionTolerance(int tolerance) {

    }

    @Override
    public int getTargetPositionTolerance() {
        return 0;
    }

    @Override
    public double getCurrent(CurrentUnit unit) {
        return 0;
    }

    @Override
    public double getCurrentAlert(CurrentUnit unit) {
        return 0;
    }

    @Override
    public void setCurrentAlert(double current, CurrentUnit unit) {

    }

    @Override
    public boolean isOverCurrent() {
        return false;
    }

    @Override
    public MotorConfigurationType getMotorType() {
        return null;
    }

    @Override
    public void setMotorType(MotorConfigurationType motorType) {

    }

    @Override
    public DcMotorController getController() {
        return null;
    }

    @Override
    public int getPortNumber() {
        return 0;
    }

    @Override
    public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        powerBehavior = zeroPowerBehavior;
    }

    @Override
    public ZeroPowerBehavior getZeroPowerBehavior() {
        return powerBehavior;
    }

    @Override
    public void setPowerFloat() {

    }

    @Override
    public boolean getPowerFloat() {
        return false;
    }

    @Override
    public void setTargetPosition(int position) {
        targetPosition = position;
    }

    @Override
    public int getTargetPosition() {
        return targetPosition;
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    public int getCurrentPosition() {
        updateCurrentPosition();
        return currentPosition;
    }

    @Override
    public void setMode(RunMode mode) {
        currentRunMode = mode;
        if (currentRunMode == RunMode.STOP_AND_RESET_ENCODER) {
            updateCurrentPosition();
            currentPosition = 0;
            currentPower = 0;
        }
    }

    @Override
    public RunMode getMode() {
        return currentRunMode;
    }

    @Override
    public void setDirection(Direction direction) {
        currentDirection = direction;
    }

    @Override
    public Direction getDirection() {
        return currentDirection;
    }

    @Override
    public void setPower(double power) {
        if (power <= 1.0 && power >= -1.0) {
            updateCurrentPosition();
            currentPower = power;
        } else {
            throw new IllegalArgumentException("Current power not between -1 and 1 (inclusive). Attempted power was " + power);
        }
    }

    @Override
    public double getPower() {
        return currentPower;
    }

    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.Other;
    }

    @Override
    public String getDeviceName() {
        return null;
    }

    @Override
    public String getConnectionInfo() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {

    }

    @Override
    public void close() {

    }

    private void updateCurrentPosition() {
        currentPosition += (int) ((currentPower * timer.milliseconds()) * 500);
        timer.reset();
    }
}
