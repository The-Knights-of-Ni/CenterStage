package org.firstinspires.ftc.teamcode.Subsystems.Drive;

public class SwerveGeneric<M, S> {
    public M motor;
    public S servoOne;
    public S servoTwo;

    public SwerveGeneric(M motor, S servoOne, S servoTwo) {
        this.motor = motor;
        this.servoOne = servoOne;
        this.servoTwo = servoTwo;
    }

    @Override
    public String toString() {
        return "SwerveGeneric{" +
                "motor=" + motor +
                ", servoOne=" + servoOne +
                ", servoTwo=" + servoTwo +
                '}';
    }
}
