package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import org.firstinspires.ftc.teamcode.Geometry.Circle;
import org.firstinspires.ftc.teamcode.Geometry.Line;
import org.firstinspires.ftc.teamcode.Geometry.Path;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.Comparator;

public class PurePursuit implements Targeter {
    Path path;
    double lookaheadDistance;

    public PurePursuit(Path path, double lookaheadDistance) {
        this.path = path;
        this.lookaheadDistance = lookaheadDistance;
    }

    public Pose getTarget(Pose currentPosition) {
        var circle = new Circle(currentPosition.getCoordinate(), lookaheadDistance);
        var target = path.end().getCoordinate();
        var targetWaypoint = path.end();
        for (Line lineSegment : path.lines) {
            target = circle.segmentIntersections(lineSegment) // Gets the intersections
                    .stream() // Converts to stream (for comparing)
                    .max(Comparator.comparingDouble(v -> v.distance(lineSegment.end))) // Finds the one closest to the end of the line.
                    .orElse(target); // If there isn't one, keep target asis.
        }
        return new Pose(target, targetWaypoint.heading);
    }

    @Override
    public boolean reachedTarget(Pose currentPosition) {
        return path.end().fuzzyCompare(currentPosition);
    }
}
