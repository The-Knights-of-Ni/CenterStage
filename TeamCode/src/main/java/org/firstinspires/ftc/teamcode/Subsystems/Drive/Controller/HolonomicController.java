package org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller;

import org.firstinspires.ftc.teamcode.Util.Pose;

/**
 * A position controller is one that uses a PID controller to control the robot's position.
 */
public interface HolonomicController extends Controller {
    /**
     * Calculates the power required to move the robot to the target position.
     * @param current The current position of the robot.
     * @param target The target position of the robot.
     * @return The power required to move the robot to the target position in each axis.
     */
    HolonomicControllerOutput calculate(Pose current, Pose target);

    /**
     * Resets the PID controller for the heading axis. This can reduce integral windup when applied properly.
     */
    void resetHeadingPID();
}
