package org.firstinspires.ftc.teamcode.DriveControl;

import org.firstinspires.ftc.teamcode.Util.Coordinate;

public class Waypoint {
    public Coordinate coordinate;
    public final boolean fullStop;

    public Waypoint(Coordinate c, boolean stop) {
        coordinate = c;
        fullStop = stop;
    }

}
