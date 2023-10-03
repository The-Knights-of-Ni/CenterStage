package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import org.firstinspires.ftc.teamcode.Geometry.Circle;
import org.firstinspires.ftc.teamcode.Geometry.Line;
import org.firstinspires.ftc.teamcode.Geometry.Path;
import org.firstinspires.ftc.teamcode.Util.Pose;
import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.List;

public class PurePursuit implements Targeter {
    Path path;
    double lookaheadDistance;

    public PurePursuit(Path path, double lookaheadDistance) {
        this.path = path;
        this.lookaheadDistance = lookaheadDistance;
    }

    public Pose getTarget(Pose currentPosition) {
        Circle circle = new Circle(currentPosition.getCoordinate(), lookaheadDistance);
        Vector target = path.end().getCoordinate();
        Pose targetWaypoint = path.end();
        for (Line lineSegment : path.lines) {
            List<Vector> intersections = circle.segmentIntersections(lineSegment);
            double bestIntersectionDistance = Double.MAX_VALUE;
            Vector bestIntersection = null;
            for (Vector intersection : intersections) {
                if (intersection.distance(lineSegment.end) < bestIntersectionDistance) {
                    bestIntersection = intersection;
                    bestIntersectionDistance = intersection.distance(lineSegment.end);
                }
            }
            if (bestIntersection != null) {
                target = bestIntersection;
            }
        }
        return new Pose(target, targetWaypoint.heading);
    }

    @Override
    public boolean reachedTarget(Pose currentPosition) {
        return path.end().fuzzyCompare(currentPosition);
    }
}
