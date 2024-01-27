package org.firstinspires.ftc.teamcode.Subsystems.Drive.PoseEstimation;

import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.*;
import org.firstinspires.ftc.teamcode.Util.Pose;

public class IMU implements PoseEstimationMethod {
    Pose startingPose;
    public BNO055IMU imu;

    public IMU(BNO055IMU imu) {
        this.imu = imu;
    }

    @Override
    public void start() {
        imu.startAccelerationIntegration(new Position(DistanceUnit.MM, 0, 0, 0, 1), new Velocity(DistanceUnit.MM, 0, 0, 0, 1), 100);
        startingPose = new Pose(imu.getPosition().toUnit(DistanceUnit.MM).x, imu.getPosition().toUnit(DistanceUnit.MM).y, imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle);
    }

    @Override
    public void update() {

    }

    @Override
    public void stop() {
        imu.stopAccelerationIntegration();
    }

    @Override
    public Pose getPose() {
        return null;
    }
}
