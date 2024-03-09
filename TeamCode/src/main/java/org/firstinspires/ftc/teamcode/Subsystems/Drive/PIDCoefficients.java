package org.firstinspires.ftc.teamcode.Subsystems.Drive;

public class PIDCoefficients<V>{
    public V kP;
    public V kI;
    public V kD;

    public PIDCoefficients(V kP, V kI, V kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    @Override
    public String toString() {
        return "PIDCoefficients{" +
                "kP=" + kP +
                ", kI=" + kI +
                ", kD=" + kD +
                '}';
    }
}
