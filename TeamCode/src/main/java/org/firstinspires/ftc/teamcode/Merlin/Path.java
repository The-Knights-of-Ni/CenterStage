package org.firstinspires.ftc.teamcode.Merlin;

import org.firstinspires.ftc.teamcode.Merlin.Curve.Arclength;

import java.util.List;

public class Path {
    private final List<PositionPath<Arclength>> segments;
    private int current = 0;

    public Path(List<PositionPath<Arclength>> segments) {
        this.segments = segments;
    }

    public void next() {
        current++;
    }

    public int getCurrentInt() {
        return current;
    }

    public PositionPath<Arclength> getCurrent() {
        return segments.get(current);
    }
}
