package org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller;

import org.firstinspires.ftc.teamcode.Subsystems.Drive.Drive;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.PID;
import org.firstinspires.ftc.teamcode.Util.Pose;

/**
 * A controller that takes in a target position and a current position and outputs the power
 * required to move the robot to the target position.
 * Holonomic means that the robot can move in any direction without changing its orientation.
 * This uses a {@link PID} controller to control the robot's movement.
 */
public class HolonomicPositionController implements PositionController {
    PID xControl;
    PID yControl;
    PID thetaControl;

    /**
     * The PID controllers for the x, y, and theta axes.
     * @param x The PID controller for the x-axis. The coefficients for this PID controller should be close to the y coefficents.
     * @param y The PID controller for the y-axis. The coefficients for this PID controller should be close to the x coefficents.
     * @param theta The PID controller for the theta-axis. The coefficients for this PID controller will be very different from the x and y coefficents.
     */
    public HolonomicPositionController(PID x, PID y, PID theta) {
        // Three PID controllers for actual robot movement instead of motor movement
        this.xControl = x;
        this.yControl = y;
        this.thetaControl = theta;
    }

    /**
     * Calculates the power required to move the robot to the target position.
     * @param current The current position of the robot.
     * @param target The target position of the robot.
     * @return The power required to move the robot to the target position in each axis.
     */
    @Override
    public ControllerOutput calculate(Pose current, Pose target) {
        var xPower = xControl.calculate(target.x * Drive.COUNTS_PER_MM, current.x * Drive.COUNTS_PER_MM);
        var yPower = yControl.calculate(target.y * Drive.COUNTS_PER_MM, current.y * Drive.COUNTS_PER_MM);
        var thetaPower = thetaControl.calculate(target.heading * Drive.COUNTS_PER_MM, current.heading * Drive.COUNTS_PER_MM);
        return new ControllerOutput(xPower, yPower, thetaPower, current);
    }

    /**
     * Resets the PID controller for the heading axis. This can reduce integral windup when applied properly.
     */
    @Override
    public void resetHeadingPID() {
        thetaControl.reset();
    }
}
