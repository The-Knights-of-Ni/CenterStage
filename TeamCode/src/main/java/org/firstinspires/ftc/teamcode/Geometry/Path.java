package org.firstinspires.ftc.teamcode.Geometry;

import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.ArrayList;

public class Path {
    public ArrayList<Waypoint> waypoints;
    public ArrayList<Line> lines;


    public Path(ArrayList<Waypoint> stops) {
        waypoints = stops;
        Waypoint previousWaypoint = null;
        for (Waypoint waypoint: waypoints) {
            if (previousWaypoint != null) {
                lines.add(new Line(previousWaypoint.pose.getCoordinate(), waypoint.pose.getCoordinate()));
            }
            previousWaypoint = waypoint;
        }

    }

    public Waypoint end() {
        return waypoints.get(waypoints.size() - 1);
    }
}
