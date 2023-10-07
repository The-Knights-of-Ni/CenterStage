package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import org.firstinspires.ftc.teamcode.Util.Pose;

public interface Controller {
    MotorGeneric<Double> calculate(Pose current, Pose target);
}
