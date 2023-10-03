package org.firstinspires.ftc.teamcode.Geometry;

import org.firstinspires.ftc.teamcode.Util.Pose;

import java.util.ArrayList;

public class Path {
    public ArrayList<Pose> waypoints;
    public ArrayList<Line> lines;


    public Path(ArrayList<Pose> stops) {
        waypoints = stops;
        Pose previousWaypoint = null;
        for (Pose waypoint : waypoints) {
            if (previousWaypoint != null) {
                lines.add(new Line(waypoint.getCoordinate(), waypoint.getCoordinate()));
            }
            previousWaypoint = waypoint;
        }

    }

    public Pose end() {
        return waypoints.get(waypoints.size() - 1);
    }
}
