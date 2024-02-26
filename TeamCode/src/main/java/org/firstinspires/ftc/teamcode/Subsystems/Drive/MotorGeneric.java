package org.firstinspires.ftc.teamcode.Subsystems.Drive;

/**
 * A generic class for storing values that correspond to motors, a replacement for a four-element array.
 *
 * @param <T> The type of the motor value.
 */
public class MotorGeneric<T> {
    public T frontLeft;
    public T frontRight;
    public T rearLeft;
    public T rearRight;


    public MotorGeneric(T frontLeft, T frontRight, T rearLeft, T rearRight) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.rearLeft = rearLeft;
        this.rearRight = rearRight;
    }

    @Override
    public String toString() {
        return "MotorGeneric{" +
                "frontLeft=" + frontLeft +
                ", frontRight=" + frontRight +
                ", rearLeft=" + rearLeft +
                ", rearRight=" + rearRight +
                '}';
    }
}
