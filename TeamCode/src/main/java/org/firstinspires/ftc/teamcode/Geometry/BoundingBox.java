package org.firstinspires.ftc.teamcode.Geometry;

import org.apache.commons.geometry.euclidean.twod.ConvexArea;
import org.apache.commons.geometry.euclidean.twod.Vector2D;

import java.util.List;

import org.firstinspires.ftc.teamcode.Util.Vector;

public class BoundingBox {
    public final ConvexArea area;


    /**
     * The bounding box generates a box to represent an objects position.
     * @param bounds The boundary of the object
     */
    public BoundingBox(ConvexArea bounds) {
        area = bounds;
    }

    public boolean contains(Vector coordinate) {
        Vector2D point = Vector2D.of(coordinate.getX(), coordinate.getY());
        return area.contains(point);
    }

    public Vector distanceFrom(BoundingBox other) {
        List<Vector2D> vertices = area.getVertices();
        List<Vector2D> otherVertices = other.area.getVertices();
        double smallestDistance = vertices.get(0).distance(otherVertices.get(0));
        Vector2D vertex1 = vertices.get(0);
        Vector2D vertex2 = otherVertices.get(0);
        for (Vector2D vertex:vertices) {
            for (Vector2D otherVertex:otherVertices) {
                if (vertex.distance(otherVertex) < smallestDistance) {
                    smallestDistance = vertex.distance(otherVertex);
                    vertex1 = vertex;
                    vertex2 = otherVertex;
                }
            }
        }
        return new Vector(Math.abs(vertex1.getX() - vertex2.getX()), Math.abs(vertex1.getY() - vertex2.getY()));
    }

    public Vector transformTo(BoundingBox other) {
        List<Vector2D> bounds = area.getBoundaryPaths().get(1).getVertexSequence();
        List<Vector2D> otherBounds = other.area.getBoundaryPaths().get(1).getVertexSequence();
        if (bounds.size() != otherBounds.size()) {
            return null;
        }
        Vector2D first = bounds.get(0);
        if (!otherBounds.contains(first)) {
            return null;
        }
        int shiftedStart = otherBounds.indexOf(first);
        for (int i = 0; i<bounds.size(); i++) {
            if (bounds.get(i) != otherBounds.get((i+shiftedStart) % bounds.size())) {
                return null;
            }
        }
        Vector2D result = bounds.get(0).vectorTo(otherBounds.get(0));
        return new Vector(result.getX(), result.getY());
    }
}
