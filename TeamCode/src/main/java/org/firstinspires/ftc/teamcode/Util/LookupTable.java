package org.firstinspires.ftc.teamcode.Util;

import java.util.Objects;
import java.util.TreeMap;
import java.util.Map;

public class LookupTable {
    private TreeMap<Double, Double> table;

    public LookupTable() {
        table = new TreeMap<>();
    }

    public LookupTable(TreeMap<Double, Double> table) {
        this.table = table;
    }

    public void addPoint(double key, double value) {
        table.put(key, value);
    }

    public void removePoint(double key) {
        table.remove(key);
    }

    public double interpolate(double key) {
        Map.Entry<Double, Double> low = table.floorEntry(key);
        Map.Entry<Double, Double> high = table.ceilingEntry(key);

        if (low != null && high != null) {
            if (Objects.equals(low.getKey(), high.getKey())) {
                return low.getValue();
            } else {
                double xDiff = high.getKey() - low.getKey();
                double yDiff = high.getValue() - low.getValue();
                double ratio = (key - low.getKey()) / xDiff;
                return low.getValue() + ratio * yDiff;
            }
        } else if (low != null) {
            return low.getValue();
        } else if (high != null) {
            return high.getValue();
        } else {
            throw new IllegalArgumentException("No data in lookup table. Internal table has length 0.");
        }
    }
}
