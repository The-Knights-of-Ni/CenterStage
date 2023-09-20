package org.firstinspires.ftc.teamcode.Merlin;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualNum;
import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualVector;
import org.firstinspires.ftc.teamcode.Merlin.Curve.*;
import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.ArrayList;
import java.util.List;

public class PathBuilder {
    private List<PositionPath<Arclength>> segments;
    private Vector last;
    private Vector lastTangent;

    public PathBuilder(List<PositionPath<Arclength>> segments, Vector last, Vector lastTangent) {
        this.segments = segments;
        this.last = last;
        this.lastTangent = lastTangent;
    }

    public PathBuilder() {
        this.segments = new ArrayList<>();
        this.last = new Vector(0, 0);
        this.lastTangent = new Vector(0, 1);
    }

    public PathBuilder(Vector start) {
        this.segments = new ArrayList<>();
        this.last = start;
        this.lastTangent = new Vector(0, 1);
    }

    private void addSegment(PositionPath<Arclength> segment, Vector end, Vector tangent) {
        segments.add(segment);
        last = end;
        lastTangent = tangent;
    }

    public PathBuilder vectorTo(Vector v) {
        addSegment(new Line(last, new Vector(v.subtract(last))), v, lastTangent);
        return this;
    }

    public PathBuilder splineTo(Vector v, Vector tangent) {
        double dist = (v.subtract(last)).getNorm();

        // NOTE: First derivatives will be normalized by arc length reparam, so the magnitudes need not match at knots.
        Vector beginDeriv = new Vector(lastTangent.scalarMultiply(dist));
        Vector endDeriv = new Vector(tangent.scalarMultiply(dist));

        ArclengthReparamCurve2d spline = new ArclengthReparamCurve2d(
                new QuinticSpline2d(
                        new DualVector<>(
                                new DualNum<>(new double[]{last.getX(), beginDeriv.getX(), 0.0}),
                                new DualNum<>(new double[]{v.getX(), endDeriv.getX(), 0.0})
                        ),
                        new DualVector<>(
                                new DualNum<>(new double[]{last.getY(), beginDeriv.getY(), 0.0}),
                                new DualNum<>(new double[]{v.getY(), endDeriv.getY(), 0.0})
                        )
                ), MathUtils.EPS
        );
        addSegment(spline, v, tangent);
        return this;
    }

    public Path build() {
        return new Path(segments);
    }
}
