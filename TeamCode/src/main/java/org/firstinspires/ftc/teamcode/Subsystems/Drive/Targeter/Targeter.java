package org.firstinspires.ftc.teamcode.Subsystems.Drive.Targeter;

import org.firstinspires.ftc.teamcode.Util.Pose;

public interface Targeter {
    Pose getTarget(Pose currentPosition);

    boolean reachedTarget(Pose currentPosition);
}
