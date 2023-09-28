package org.firstinspires.ftc.teamcode.Merlin.Profile;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static List<Double> timeScan(DisplacementProfile p) {
        List<Double> times = new ArrayList<>(1);
        times.add(0.0);
        for (int i: p.accels.indices) {
            double toAdd;
            if (p.accels.get(i) == 0.0) {
                toAdd=(p.disps.get(i + 1) - p.disps.get(i)) / p.vels.get(i)
            } else {
                toAdd = (p.vels.get(i + 1) - p.vels.get(i)) / p.accels.get(i)
            }
            times.add(
                times.get(times.size() - 1) + toAdd
            );
        }
        return times;
    }
}
