package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import org.firstinspires.ftc.teamcode.Util.Pose;

public class StaticTargeter implements Targeter {
    Pose target;

    public StaticTargeter(Pose target) {
        this.target = target;
    }

    @Override
    public Pose getTarget(Pose currentPosition) {
        return target;
    }

    @Override
    public boolean reachedTarget(Pose currentPosition) {
        return target.fuzzyCompare(currentPosition);
    }
}
