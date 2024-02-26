package org.firstinspires.ftc.teamcode.Subsystems.Drive.Targeter;

import org.firstinspires.ftc.teamcode.Util.Pose;

/**
 * A targeter tells the robot where to go.
 * The robot will move in the direction of the target until it reaches it,
 * but you may change the location of the target at any time.
 */
public interface Targeter {
    Pose getTarget(Pose currentPosition);

    boolean reachedTarget(Pose currentPosition);
}
