package org.firstinspires.ftc.teamcode.Merlin.Curve;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualVector;
import org.firstinspires.ftc.teamcode.Merlin.PositionPath;

import java.util.List;

public class CompositePositionPath<Param> extends PositionPath<Param> {
    List<PositionPath<Param>> paths;
    List<Double> offsets;
    double length;

    public CompositePositionPath(List<PositionPath<Param>> paths, List<Double> offsets) {
        this.paths = paths;
        this.offsets = offsets;
        length = offsets.get(offsets.size() - 1);
        assert !paths.isEmpty();
    }

    public DualVector<Param> get(double param, int n) {
        if (param > length) {
            return DualVector.constant(paths.get(paths.size()-1).end(1).value(), n);
        }
        for (int i = offsets.size()-1; i >= 0; i--) {
            double offset = offsets.get(i);
            PositionPath<Param> path = paths.get(i);
            if (param>=offset) {
                return path.get(param - offset, n);
            }
        }
        return DualVector.constant(paths.get(0).get(0.0, 1).value(), n);
    }

    public double length() {
        return length;
    }
}
