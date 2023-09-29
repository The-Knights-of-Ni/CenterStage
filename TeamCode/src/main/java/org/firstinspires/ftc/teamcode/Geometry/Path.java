package org.firstinspires.ftc.teamcode.Geometry;

import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.ArrayList;

public class Path {
    ArrayList<Waypoint> waypoints;
    ArrayList<Waypoint> poses;
    int currentWaypoint;
    int targetWaypoint;


    public Path(ArrayList<Waypoint> stops) {
        waypoints = stops;
        currentWaypoint = 0;
        targetWaypoint = 1;
    }

    public void next() {
        currentWaypoint++;
        targetWaypoint++;
    }
    public Vector goToNextWaypoint() {
        return new Vector(
                Math.abs(waypoints.get(currentWaypoint).coordinate.getX() - waypoints.get(targetWaypoint).coordinate.getX()),
                Math.abs(waypoints.get(currentWaypoint).coordinate.getY() - waypoints.get(targetWaypoint).coordinate.getY())
        );
    }

    public Vector getVectorForPosition(Vector coordinate) {
        return new Vector(Math.abs(coordinate.getX() - waypoints.get(targetWaypoint).coordinate.getX()),
                Math.abs(coordinate.getY() - waypoints.get(targetWaypoint).coordinate.getY()));
    }
}
