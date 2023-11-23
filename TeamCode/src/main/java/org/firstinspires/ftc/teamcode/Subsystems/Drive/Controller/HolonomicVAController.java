package org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller;

import org.firstinspires.ftc.teamcode.Subsystems.Drive.FeedForward;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.MotionProfile.MotionProfileOutput;

public class HolonomicVAController implements VAController {
    FeedForward xControl;
    FeedForward yControl;
    FeedForward thetaControl;

    public HolonomicVAController(FeedForward x, FeedForward y, FeedForward theta) {
        // Three PID controllers for actual robot movement instead of motor movement

        this.xControl = x;
        this.yControl = y;
        this.thetaControl = theta;
    }

    @Override
    public ControllerOutput calculate(double heading, MotionProfileOutput target) {
        var xPower = xControl.calculate(target.x.velocity, target.x.velocity);
        var yPower = yControl.calculate(target.y.velocity, target.y.velocity);
        var thetaPower = thetaControl.calculate(target.heading.velocity, target.heading.velocity);
        return new ControllerOutput(xPower, yPower, thetaPower, heading);
    }
}
