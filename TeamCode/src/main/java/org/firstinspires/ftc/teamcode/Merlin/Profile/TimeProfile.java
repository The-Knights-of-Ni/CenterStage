package org.firstinspires.ftc.teamcode.Merlin.Profile;

import org.firstinspires.ftc.teamcode.Merlin.AutoDifferentiation.DualNum;

import java.util.List;

import static org.firstinspires.ftc.teamcode.Merlin.Profile.Util.timeScan;

public class TimeProfile {
    DisplacementProfile dispProfile;
    List<Double> times;
    public double duration;
    public TimeProfile(DisplacementProfile displacementProfile) {
        this.dispProfile = displacementProfile;
        times = timeScan(displacementProfile);
        duration = times.get(times.size()-1);
        assert times.size() == dispProfile.disps.size();
    }

    public DualNum<Time> get(double t) {
        int index = times.indexOf(t);
        return when {
            index >= times.lastIndex ->
                DualNum(
                    doubleArrayOf(
                        dispProfile.disps[index], dispProfile.vels[index], 0.0
                    )
                )
            index >= 0 ->
                DualNum(
                    doubleArrayOf(
                        dispProfile.disps[index], dispProfile.vels[index], dispProfile.accels[index]
                    )
                )
            else -> {
                val insIndex = -(index + 1)
                when {
                    insIndex <= 0 -> {
                        val v = dispProfile.vels.first()
                        DualNum(doubleArrayOf(v * t, v, 0.0))
                    }
                    insIndex >= times.size -> {
                        val v = dispProfile.vels.last()
                        DualNum(doubleArrayOf(dispProfile.length + v * (t - duration), v, 0.0))
                    }
                    else -> {
                        val dt = t - times[insIndex - 1]
                        val x0 = dispProfile.disps[insIndex - 1]
                        val v0 = dispProfile.vels[insIndex - 1]
                        val a = dispProfile.accels[insIndex - 1]

                        DualNum(
                            doubleArrayOf(
                                (0.5 * a * dt + v0) * dt + x0,
                                a * dt + v0,
                                a
                            )
                        )
                    }
                }
            }
        }
    }

    public double inverse(double x) {
        val index = dispProfile.disps.binarySearch(x)
        return when {
            index >= dispProfile.disps.lastIndex -> times[index]
            index >= 0 -> times[index]
            else -> {
                val insIndex = -(index + 1)
                when {
                    insIndex <= 0 -> 0.0
                    insIndex >= times.size -> duration
                    else -> {
                        val dx = x - dispProfile.disps[insIndex - 1]
                        val t0 = times[insIndex - 1]
                        val v0 = dispProfile.vels[insIndex - 1]
                        val a = dispProfile.accels[insIndex - 1]

                        if (a == 0.0) {
                            t0 + dx / v0
                        } else {
                            t0 + sqrt(((v0 * v0 / a) + 2 * dx) / a).withSign(a) - v0 / a
                        }
                    }
                }
            }
        }
    }
}
