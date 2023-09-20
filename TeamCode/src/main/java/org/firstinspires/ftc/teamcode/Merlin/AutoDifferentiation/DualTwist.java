package org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation;

import org.firstinspires.ftc.teamcode.Util.Twist;

public class DualTwist<Param> {
    public final DualVector<Param> line;
    public final DualNum<Param> angle;

    public DualTwist(DualVector<Param> line, DualNum<Param> angle) {
        this.line = line;
        this.angle = angle;
    }

    public Twist value() {
        return new Twist(line.value(), angle.value());
    }

    public DualPoseVelocity<Param> velocity() {
        return new DualPoseVelocity<>(line.drop(1), angle.drop(1));
    }
}