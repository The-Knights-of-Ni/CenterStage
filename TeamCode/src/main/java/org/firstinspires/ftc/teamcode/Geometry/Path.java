package org.firstinspires.ftc.teamcode.Geometry;

import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.ArrayList;

public class Path {
    public ArrayList<Waypoint> waypoints;
    public ArrayList<Line> lines;


    public Path(ArrayList<Waypoint> stops) {
        waypoints = stops;

    }

    public Waypoint end() {
        return waypoints.get(waypoints.size() - 1);
    }
}
