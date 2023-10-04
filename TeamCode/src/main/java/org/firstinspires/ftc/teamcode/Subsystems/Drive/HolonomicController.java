package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import org.firstinspires.ftc.teamcode.Util.Pose;

public class HolonomicController implements Controller {
    PID xControl;
    PID yControl;
    PID thetaControl;

    public HolonomicController(PID x, PID y, PID theta) {
        // Three PID controllers for actual robot movement instead of motor movement

        this.xControl = x;
        this.yControl = y;
        this.thetaControl = theta;
    }


    @Override
    public MotorGeneric<Double> calculate(Pose target, Pose current) {
        double xPower = xControl.calculate(target.x * Drive.COUNTS_PER_MM, current.x * Drive.COUNTS_PER_MM);
        double yPower = yControl.calculate(target.y * Drive.COUNTS_PER_MM, current.y * Drive.COUNTS_PER_MM);
        double thetaPower = thetaControl.calculate(target.heading * Drive.COUNTS_PER_MM, current.heading * Drive.COUNTS_PER_MM); // TODO: Should be angular
        double xRotated = xPower * Math.cos(target.heading) - yPower * Math.sin(target.heading);
        double yRotated = xPower * Math.sin(target.heading) + yPower * Math.cos(target.heading);
        return new MotorGeneric<>((xRotated + yRotated + thetaPower),
                (xRotated - yRotated - thetaPower),
                (xRotated - yRotated + thetaPower),
                (xRotated + yRotated - thetaPower));
    }
}
