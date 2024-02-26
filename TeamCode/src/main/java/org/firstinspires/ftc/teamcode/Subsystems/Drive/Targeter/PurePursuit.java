package org.firstinspires.ftc.teamcode.Subsystems.Drive.Targeter;

import com.sun.tools.javac.util.List;
import org.firstinspires.ftc.teamcode.Geometry.Circle;
import org.firstinspires.ftc.teamcode.Geometry.Line;
import org.firstinspires.ftc.teamcode.Geometry.Path;
import org.firstinspires.ftc.teamcode.Util.Pose;

import java.util.Comparator;

/**
 * A pure pursuit targeter. This targeter uses a lookahead distance to find the target point on a path.
 * <p>
 * The targeter draws a circle around the robot with the lookahead distance as the radius.
 * It then finds the furthest intersection of the circle with the path, and sends the robot in that direction.
 * This way the robot can follow a straight path while moving in a curve,
 * this is very useful for non-holonomic drives like swerve.
 */
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
            target = List.from(circle.segmentIntersections(lineSegment)) // Gets the intersections
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
