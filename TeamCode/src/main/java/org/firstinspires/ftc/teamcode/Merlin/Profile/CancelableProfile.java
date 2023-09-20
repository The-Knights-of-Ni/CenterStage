package org.firstinspires.ftc.teamcode.Merlin.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CancelableProfile {
    public final DisplacementProfile baseProfile;
    public final List<Double> disps;
    public final List<Double> minAccels;

    public CancelableProfile(DisplacementProfile baseProfile, List<Double> disps, List<Double> minAccels) {
        this.baseProfile = baseProfile;
        this.disps = disps;
        this.minAccels = minAccels;
    }

    public DisplacementProfile cancel(double x) {
        List<Double> newDisps = new ArrayList<>();
        List<Double> vels = new ArrayList<>();
        List<Double> accels = new ArrayList<>();

        newDisps.add(0.0);
        vels.add(baseProfile.get(x).get(1));

        int rawIndex = Collections.binarySearch(disps, x);
        int beginIndex = (rawIndex >= 0) ? rawIndex : Math.max(1, -(rawIndex + 1));

        double targetVel = baseProfile.vels.get(baseProfile.vels.size() - 1);

        for (int index = beginIndex; index <= disps.size() - 1; index++) {
            double v = vels.get(vels.size() - 1);
            double a = minAccels.get(index - 1);

            double targetDisp = newDisps.get(newDisps.size() - 1) + (targetVel * targetVel - v * v) / (2 * a);

            if (x + targetDisp > disps.get(index)) {
                newDisps.add(disps.get(index) - x);
                vels.add(Math.sqrt(v * v + 2 * a * (disps.get(index) - disps.get(index - 1))));
                accels.add(a);
            } else {
                newDisps.add(targetDisp);
                vels.add(targetVel);
                accels.add(a);
                break;
            }
        }

        return new DisplacementProfile(newDisps, vels, accels);
    }
}
