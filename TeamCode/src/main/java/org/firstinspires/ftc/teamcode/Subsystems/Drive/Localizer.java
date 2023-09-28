package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualTwist;
import org.firstinspires.ftc.teamcode.Merlin.Profile.Time;

public interface Localizer {
    DualTwist<Time> update();
}
