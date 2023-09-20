package org.firstinspires.ftc.teamcode.Merlin;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.*;
import org.firstinspires.ftc.teamcode.Merlin.Curve.Arclength;
import org.firstinspires.ftc.teamcode.Util.Twist;

import java.util.Arrays;
import java.util.List;

public class MecanumKinematics {
    public final double trackWidth;
    public final double lateralMultiplier;

    public MecanumKinematics(double trackWidth, double lateralMultiplier) {
        this.trackWidth = trackWidth;
        this.lateralMultiplier = lateralMultiplier;
    }

    public MecanumKinematics(double trackWidth, double wheelbase, double lateralMultiplier) {
        this(trackWidth + wheelbase / 2, lateralMultiplier);
    }

    public static class WheelIncrements<Param> {
        public final DualNum<Param> leftFront;
        public final DualNum<Param> leftBack;
        public final DualNum<Param> rightBack;
        public final DualNum<Param> rightFront;

        public WheelIncrements(
                DualNum<Param> leftFront,
                DualNum<Param> leftBack,
                DualNum<Param> rightBack,
                DualNum<Param> rightFront
        ) {
            this.leftFront = leftFront;
            this.leftBack = leftBack;
            this.rightBack = rightBack;
            this.rightFront = rightFront;
        }
    }

    public <Param> DualTwist<Param> forward(WheelIncrements<Param> w) {
        DualVector<Param> linearVel = new DualVector<>(
                w.leftFront.add(w.leftBack).add(w.rightBack).add(w.rightFront).multiply(0.25),
                w.leftFront.subtract(w.leftBack).subtract(w.rightBack).add(w.rightFront).multiply(0.25 / lateralMultiplier)
        );
        double angVel = w.leftFront.negate().subtract(w.leftBack).add(w.rightBack).add(w.rightFront).multiply(0.25 / trackWidth);

        return new DualTwist<>(linearVel, angVel);
    }

    public static class WheelVelocities<Param> {
        public final DualNum<Param> leftFront;
        public final DualNum<Param> leftBack;
        public final DualNum<Param> rightBack;
        public final DualNum<Param> rightFront;

        public WheelVelocities(
                DualNum<Param> leftFront,
                DualNum<Param> leftBack,
                DualNum<Param> rightBack,
                DualNum<Param> rightFront
        ) {
            this.leftFront = leftFront;
            this.leftBack = leftBack;
            this.rightBack = rightBack;
            this.rightFront = rightFront;
        }

        public List<DualNum<Param>> all() {
            return Arrays.asList(leftFront, leftBack, rightBack, rightFront);
        }
    }

    public <Param> WheelVelocities<Param> inverse(DualPoseVelocity<Param> t) {
        DualNum<Param> linearVelX = t.linearVel.getX();
        DualNum<Param> linearVelY = t.linearVel.getY();
        DualNum<Param> angVel = t.angVel;

        return new WheelVelocities<>(
                linearVelX.subtract(linearVelY.times(lateralMultiplier)).subtract(angVel.times(trackWidth)),
                linearVelX.add(linearVelY.times(lateralMultiplier)).subtract(angVel.times(trackWidth)),
                linearVelX.subtract(linearVelY.times(lateralMultiplier)).add(angVel.times(trackWidth)),
                linearVelX.add(linearVelY.times(lateralMultiplier)).add(angVel.times(trackWidth))
        );
    }

    public class WheelVelConstraint implements VelConstraint {
        private final double maxWheelVel;

        public WheelVelConstraint(double maxWheelVel) {
            this.maxWheelVel = maxWheelVel;
        }

        @Override
        public double maxRobotVel(DualPose<Arclength> robotPose, PosePath path, double s) {
            DualPose<Arclength> txRobotWorld = robotPose.value().inverse();
            Twist robotVelWorld = robotPose.velocity().value();
            Twist robotVelRobot = txRobotWorld.transformBy(robotVelWorld);

            WheelVelocities<Arclength> wheelVelocities = inverse(
                    new DualPoseVelocity<>(robotVelRobot, new DualNum<>(1.0))
            );

            double minWheelVel = Double.MAX_VALUE;
            for (DualNum<Arclength> wheelVel : wheelVelocities.all()) {
                double absWheelVel = Math.abs(maxWheelVel / wheelVel.value());
                minWheelVel = Math.min(minWheelVel, absWheelVel);
            }

            return minWheelVel;
        }
    }
}