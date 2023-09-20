package org.firstinspires.ftc.teamcode.Util;

public class PoseVelocity {
    public final Vector linearVel;
    public final double angVel;

    public PoseVelocity(Vector linearVel, double angVel) {
        this.linearVel = linearVel;
        this.angVel = angVel;
    }

    public PoseVelocity minus(PoseVelocity pv) {
        return new PoseVelocity(linearVel.minus(pv.linearVel), angVel - pv.angVel);
    }
}

