package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualNum;
import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualPose;
import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualPoseVelocity;
import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualTwist;
import org.firstinspires.ftc.teamcode.Merlin.Encoder.PositionVelocityPair;
import org.firstinspires.ftc.teamcode.Merlin.Encoder.RawEncoder;
import org.firstinspires.ftc.teamcode.Merlin.MecanumKinematics;
import org.firstinspires.ftc.teamcode.Merlin.Profile.Time;
import org.firstinspires.ftc.teamcode.Subsystems.Drive.Controller.HolonomicController;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.PoseVelocity;
import org.firstinspires.ftc.teamcode.Util.Rotation;

import java.util.LinkedList;

public final class StrippedMerlinDrive {
    public static class Params {
        // drive model parameters
        public double inPerTick = 0;
        public double lateralInPerTick = 1;
        public double trackWidthTicks = 0;

        // feedforward parameters (in tick units)
        public double kS = 0;
        public double kV = 0;
        public double kA = 0;

        // path profile parameters (in inches)
        public double maxWheelVel = 50;
        public double minProfileAccel = -30;
        public double maxProfileAccel = 50;

        // turn profile parameters (in radians)
        public double maxAngVel = Math.PI; // shared with path
        public double maxAngAccel = Math.PI;

        // path controller gains
        public double axialGain = 0.0;
        public double lateralGain = 0.0;
        public double headingGain = 0.0; // shared with turn

        public double axialVelGain = 0.0;
        public double lateralVelGain = 0.0;
        public double headingVelGain = 0.0; // shared with turn
    }

    public static Params PARAMS = new Params();

    public final MecanumKinematics kinematics = new MecanumKinematics(
            PARAMS.inPerTick * PARAMS.trackWidthTicks, PARAMS.inPerTick / PARAMS.lateralInPerTick);

    public final FeedForward feedforward = new FeedForward(PARAMS.kS, PARAMS.kV / PARAMS.inPerTick, PARAMS.kA / PARAMS.inPerTick);

    public final DcMotorEx leftFront, leftBack, rightBack, rightFront;

    public final VoltageSensor voltageSensor;

    public final IMU imu;

    public final Localizer localizer;
    public Pose pose;

    private final LinkedList<Pose> poseHistory = new LinkedList<>();

    public class DriveLocalizer implements Localizer {
        public final RawEncoder leftFront, leftRear, rightRear, rightFront;

        private int lastLeftFrontPos, lastLeftRearPos, lastRightRearPos, lastRightFrontPos;
        private Rotation lastHeading;

        public DriveLocalizer() {
            leftFront = new OverflowEncoder(new RawEncoder(MecanumDrive.this.leftFront));
            leftRear = new OverflowEncoder(new RawEncoder(MecanumDrive.this.leftBack));
            rightRear = new OverflowEncoder(new RawEncoder(MecanumDrive.this.rightBack));
            rightFront = new OverflowEncoder(new RawEncoder(MecanumDrive.this.rightFront));

            lastLeftFrontPos = leftFront.getPositionAndVelocity().position;
            lastLeftRearPos = leftRear.getPositionAndVelocity().position;
            lastRightRearPos = rightRear.getPositionAndVelocity().position;
            lastRightFrontPos = rightFront.getPositionAndVelocity().position;

            lastHeading = Rotation2d.exp(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
        }

        @Override
        public DualTwist<Time> update() {
            PositionVelocityPair leftFrontPosVel = leftFront.getPositionAndVelocity();
            PositionVelocityPair leftRearPosVel = leftRear.getPositionAndVelocity();
            PositionVelocityPair rightRearPosVel = rightRear.getPositionAndVelocity();
            PositionVelocityPair rightFrontPosVel = rightFront.getPositionAndVelocity();

            Rotation heading = Rotation2d.exp(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
            double headingDelta = heading.minus(lastHeading);

            DualTwist<Time> twist = kinematics.forward(new MecanumKinematics.WheelIncrements<>(
                    new DualNum<Time>(new double[]{
                            (leftFrontPosVel.position - lastLeftFrontPos),
                            leftFrontPosVel.velocity,
                    }).times(PARAMS.inPerTick),
                    new DualNum<Time>(new double[]{
                            (leftRearPosVel.position - lastLeftRearPos),
                            leftRearPosVel.velocity,
                    }).times(PARAMS.inPerTick),
                    new DualNum<Time>(new double[]{
                            (rightRearPosVel.position - lastRightRearPos),
                            rightRearPosVel.velocity,
                    }).times(PARAMS.inPerTick),
                    new DualNum<Time>(new double[]{
                            (rightFrontPosVel.position - lastRightFrontPos),
                            rightFrontPosVel.velocity,
                    }).times(PARAMS.inPerTick)
            ));

            lastLeftFrontPos = leftFrontPosVel.position;
            lastLeftRearPos = leftRearPosVel.position;
            lastRightRearPos = rightRearPosVel.position;
            lastRightFrontPos = rightFrontPosVel.position;

            lastHeading = heading;

            return new DualTwist<>(
                    twist.line,
                    DualNum.cons(headingDelta, twist.angle.drop(1))
            );
        }
    }

    public StrippedMerlinDrive(HardwareMap hardwareMap, Pose pose) {
        this.pose = pose;

        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);

        voltageSensor = hardwareMap.voltageSensor.iterator().next();

        localizer = new DriveLocalizer();

    }

    public void setDrivePowers(PoseVelocity powers) {
        MecanumKinematics.WheelVelocities<Time> wheelVels = new MecanumKinematics(1).inverse(
                DualPoseVelocity.constant(powers, 1));

        double maxPowerMag = 1;
        for (DualNum<Time> power : wheelVels.all()) {
            maxPowerMag = Math.max(maxPowerMag, power.value());
        }

        leftFront.setPower(wheelVels.leftFront.get(0) / maxPowerMag);
        leftBack.setPower(wheelVels.leftBack.get(0) / maxPowerMag);
        rightBack.setPower(wheelVels.rightBack.get(0) / maxPowerMag);
        rightFront.setPower(wheelVels.rightFront.get(0) / maxPowerMag);
    }

    public moveTest(TimeTrajectory timeTrajectory) {
        ElapsedTime timer = new ElapsedTime();
        double t;
        double beginTs = timer.seconds();
        t = timer.seconds() - beginTs;

        if (t >= timeTrajectory.duration) {
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightBack.setPower(0);
            rightFront.setPower(0);

            return false;
        }

        DualPose<Time> txWorldTarget = timeTrajectory.get(t);

        PoseVelocity robotVelRobot = updatePoseEstimate();

        DualPoseVelocity<Time> command = new HolonomicController(
                PARAMS.axialGain, PARAMS.lateralGain, PARAMS.headingGain,
                PARAMS.axialVelGain, PARAMS.lateralVelGain, PARAMS.headingVelGain
        )
                .compute(txWorldTarget, pose, robotVelRobot);

        MecanumKinematics.WheelVelocities<Time> wheelVels = kinematics.inverse(command);
        leftFront.setPower(feedforward.calculate(wheelVels.leftFront);
        leftBack.setPower(feedforward.calculate(wheelVels.leftBack));
        rightBack.setPower(feedforward.calculate(wheelVels.rightBack));
        rightFront.setPower(feedforward.calculate(wheelVels.rightFront));

    }


    public PoseVelocity updatePoseEstimate() {
        DualTwist<Time> twist = localizer.update();
        pose = pose.plus(twist.value());

        poseHistory.add(pose);
        while (poseHistory.size() > 100) {
            poseHistory.removeFirst();
        }

        return twist.velocity().value();
    }
}
