package org.firstinspires.ftc.teamcode.DriveControl;

import org.firstinspires.ftc.teamcode.Util.Coordinate;
import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.ArrayList;

public class Path {
    ArrayList<Waypoint> waypoints;
    int currentWaypoint;
    int targetWaypoint;


    private boolean isMoving;
    public Path(ArrayList<Waypoint> stops) {
        waypoints = stops;
        currentWaypoint = 0;
        targetWaypoint = 1;
        isMoving = false;
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

    public Vector getVectorForPosition(Coordinate coordinate) {
        return new Vector(Math.abs(coordinate.getX() - waypoints.get(targetWaypoint).coordinate.getX()),
                Math.abs(coordinate.getY() - waypoints.get(targetWaypoint).coordinate.getY()));
    }
}
