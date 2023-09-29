package org.firstinspires.ftc.teamcode.Util;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class ServoEx {

    private Servo servo;

    //always stored internally as radians
    private double maxAngle, minAngle;

    private final double maxPosition = 1;
    private final double minPosition = 0;

    public ServoEx(HardwareMap hw, String servoName, double minAngle, double maxAngle, AngleUnit angleUnit) {
        servo = hw.get(Servo.class, servoName);

        this.minAngle = toRadians(minAngle, angleUnit);
        this.maxAngle = toRadians(maxAngle, angleUnit);
    }

    public ServoEx(HardwareMap hw, String servoName, double minDegree, double maxDegree) {
        this(hw, servoName, minDegree, maxDegree, AngleUnit.DEGREES);
    }

    public void rotateByAngle(double angle, AngleUnit angleUnit) {
        angle = getAngle(angleUnit) + angle;
        turnToAngle(angle, angleUnit);
    }

    public void rotateByAngle(double degrees) {
        rotateByAngle(degrees, AngleUnit.DEGREES);
    }

    public void turnToAngle(double angle, AngleUnit angleUnit) {
        double angleRadians = Range.clip(toRadians(angle, angleUnit), minAngle, maxAngle);
        setPosition((angleRadians - minAngle) / (getAngleRange(AngleUnit.RADIANS)));
    }

    public void turnToAngle(double degrees) {
        turnToAngle(degrees, AngleUnit.DEGREES);
    }

    public void rotateBy(double position) {
        position = getPosition() + position;
        setPosition(position);
    }

    public void setPosition(double position) {
        servo.setPosition(Range.clip(position, minPosition, maxPosition));
    }

    public void setRange(double min, double max, AngleUnit angleUnit) {
        this.minAngle = toRadians(min, angleUnit);
        this.maxAngle = toRadians(max, angleUnit);
    }

    public void setRange(double min, double max) {
        setRange(min, max, AngleUnit.DEGREES);
    }

    public void setInverted(boolean isInverted) {
        servo.setDirection(isInverted ? Servo.Direction.REVERSE : Servo.Direction.FORWARD);
    }

    public boolean getInverted() {
        return Servo.Direction.REVERSE == servo.getDirection();
    }

    public double getPosition() {
        return servo.getPosition();
    }

    public double getAngle(AngleUnit angleUnit) {
        return getPosition() * getAngleRange(angleUnit) + fromRadians(minAngle, angleUnit);
    }

    public double getAngle() {
        return getAngle(AngleUnit.DEGREES);
    }

    public double getAngleRange(AngleUnit angleUnit) {
        return fromRadians(maxAngle - minAngle, angleUnit);
    }

    public double getAngleRange() {
        return getAngleRange(AngleUnit.DEGREES);
    }

    public void disable() {
        servo.close();
    }

    public String getDeviceType() {
        String port = Integer.toString(servo.getPortNumber());
        String controller = servo.getController().toString();
        return "ServoEx: " + port + "; " + controller;
    }

    private double toRadians(double angle, AngleUnit angleUnit) {
        return angleUnit == AngleUnit.DEGREES ? Math.toRadians(angle) : angle;
    }

    private double fromRadians(double angle, AngleUnit angleUnit) {
        return angleUnit == AngleUnit.DEGREES ? Math.toDegrees(angle) : angle;
    }

}