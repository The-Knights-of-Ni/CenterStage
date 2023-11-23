package org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller;

import org.firstinspires.ftc.teamcode.Subsystems.Drive.Drive;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.PID;
import org.firstinspires.ftc.teamcode.Util.Pose;

public class HolonomicPositionController implements PositionController {
    PID xControl;
    PID yControl;
    PID thetaControl;

    public HolonomicPositionController(PID x, PID y, PID theta) {
        // Three PID controllers for actual robot movement instead of motor movement

        this.xControl = x;
        this.yControl = y;
        this.thetaControl = theta;
    }

    @Override
    public ControllerOutput calculate(Pose current, Pose target) {
        var xPower = xControl.calculate(target.x * Drive.COUNTS_PER_MM, current.x * Drive.COUNTS_PER_MM);
        var yPower = yControl.calculate(target.y * Drive.COUNTS_PER_MM, current.y * Drive.COUNTS_PER_MM);
        var thetaPower = thetaControl.calculate(target.heading * Drive.COUNTS_PER_MM, current.heading * Drive.COUNTS_PER_MM);
        return new ControllerOutput(xPower, yPower, thetaPower, current.heading);
    }

    @Override
    public void resetHeadingPID() {
        thetaControl.reset();
    }
}
