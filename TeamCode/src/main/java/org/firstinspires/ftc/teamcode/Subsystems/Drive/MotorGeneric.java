package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import java.util.ArrayList;

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

    public MotorGeneric(T[] motors) {
        this(motors[0], motors[1], motors[2], motors[3]);
    }

    public MotorGeneric(ArrayList<T> motors) {
        this(motors.get(0), motors.get(1), motors.get(2), motors.get(3));
    }

    public ArrayList<T> toList() {
        ArrayList<T> arrayList = new ArrayList<>(4);
        arrayList.add(frontLeft);
        arrayList.add(frontRight);
        arrayList.add(rearLeft);
        arrayList.add(rearRight);
        return arrayList;
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
