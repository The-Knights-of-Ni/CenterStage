package org.firstinspires.ftc.teamcode.Subsystems.Drive.PoseEstimation;

import org.firstinspires.ftc.teamcode.Util.Pose;

/**
 * A PoseEstimationMethod is a class that can be used to estimate the robot's current position.
 */
public interface PoseEstimationMethod {
    void start();

    void update();

    void stop();

    /**
     * Zero cost getter for the current pose
     * @return Pose from last update
     */
    Pose getPose();
}
