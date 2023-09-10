package org.firstinspires.ftc.teamcode.DriveControl;

import org.firstinspires.ftc.teamcode.Util.Vector;

public class RobotModel {
    public Vector momentum;
    public double angle;
    public BoundingBox boundingBox;
    public RobotModel(BoundingBox initialPosition, double initialAngle) {
        boundingBox = initialPosition;
        momentum = new Vector(0,0);
        angle = initialAngle;
    }
}
