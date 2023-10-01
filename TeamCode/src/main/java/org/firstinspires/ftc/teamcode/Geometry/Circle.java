package org.firstinspires.ftc.teamcode.Geometry;

import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Circle {
    public Vector center;
    public double radius;

    public Circle(Vector center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public List<Vector> segmentIntersections(Line seg) {
        List<Vector> intersections = new ArrayList<>();

        Vector p1 = new Vector(seg.start.getX() - this.center.getX(), seg.start.getY() - this.center.getY());
        Vector p2 = new Vector(seg.end.getX() - this.center.getX(), seg.end.getY() - this.center.getY());

        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();

        double d = Math.sqrt(dx * dx + dy * dy);
        double D = p1.getX() * p2.getY() - p2.getX() * p1.getY();

        double discriminant = this.radius * this.radius * d * d - D * D;

        if (discriminant < 0) {
            return intersections;
        }

        double x1 = (D * dy + (dy >= 0 ? 1 : -1) * dx * Math.sqrt(discriminant)) / (d * d);
        double x2 = (D * dy - (dy >= 0 ? 1 : -1) * dx * Math.sqrt(discriminant)) / (d * d);

        double y1 = (-D * dx + Math.abs(dy) * Math.sqrt(discriminant)) / (d * d);
        double y2 = (-D * dx - Math.abs(dy) * Math.sqrt(discriminant)) / (d * d);

        boolean valid_intersection_1 = Math.min(p1.getX(), p2.getX()) < x1 && x1 < Math.max(p1.getX(), p2.getX()) || Math.min(p1.getY(), p2.getY()) < y1 && y1 < Math.max(p1.getY(), p2.getY());
        boolean valid_intersection_2 = Math.min(p1.getX(), p2.getX()) < x2 && x2 < Math.max(p1.getX(), p2.getX()) || Math.min(p1.getY(), p2.getY()) < y2 && y2 < Math.max(p1.getY(), p2.getY());

        if (valid_intersection_1) {
            intersections.add(new Vector(x1 + this.center.getX(), y1 + this.center.getY()));
        }

        if (valid_intersection_2) {
            intersections.add(new Vector(x2 + this.center.getX(), y2 + this.center.getY()));
        }

        return intersections;
    }
}
