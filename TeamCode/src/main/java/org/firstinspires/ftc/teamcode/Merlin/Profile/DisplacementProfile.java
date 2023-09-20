package org.firstinspires.ftc.teamcode.Merlin.Profile;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualNum;

import java.util.List;
import java.util.Collections;

public class DisplacementProfile {
    public final List<Double> disps;
    public final List<Double> vels;
    public final List<Double> accels;
    public final double length;

    public DisplacementProfile(List<Double> disps, List<Double> vels, List<Double> accels) {
        this.disps = disps;
        this.vels = vels;
        this.accels = accels;
        this.length = disps.get(disps.size() - 1);

        if (disps.size() != vels.size() || disps.size() != accels.size() + 1) {
            throw new IllegalArgumentException("Invalid input lists");
        }
    }

    public DualNum<Time> get(double x) {
        int index = Collections.binarySearch(disps, x);
        if (index >= disps.size() - 1) {
            return new DualNum<>(new double[]{x, vels.get(index), 0.0});
        } else if (index >= 0) {
            return new DualNum<>(new double[]{x, vels.get(index), accels.get(index)});
        } else {
            int insIndex = -(index + 1);
            if (insIndex <= 0) {
                return new DualNum<>(new double[]{x, vels.get(0), 0.0});
            } else if (insIndex >= disps.size()) {
                return new DualNum<>(new double[]{x, vels.get(disps.size() - 1), 0.0});
            } else {
                double dx = x - disps.get(insIndex - 1);
                double v0 = vels.get(insIndex - 1);
                double a = accels.get(insIndex - 1);

                return new DualNum<>(new double[]{x, Math.sqrt(v0 * v0 + 2 * a * dx), a});
            }
        }
    }
}