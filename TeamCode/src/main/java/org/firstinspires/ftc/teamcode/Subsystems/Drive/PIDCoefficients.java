package org.firstinspires.ftc.teamcode.Subsystems.Drive;

public class PIDCoefficients {
    public double kP;
    public double kI;
    public double kD;

    public PIDCoefficients(double kP, double kI, double kD) {
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
