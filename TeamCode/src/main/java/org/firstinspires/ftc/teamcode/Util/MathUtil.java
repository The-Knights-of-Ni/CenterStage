package org.firstinspires.ftc.teamcode.Util;

import java.util.ArrayList;
import java.util.List;

public class MathUtil {
    // TODO: is the branch here okay? would snz() on the denominator be better?
    public static double lerp(double x, double fromLo, double fromHi, double toLo, double toHi) {
        if (fromLo == fromHi) {
            return 0.0;
        } else {
            return toLo + (x - fromLo) * (toHi - toLo) / (fromHi - fromLo);
        }
    }

    /**
     * Precondition: The list is sorted
     * Searches the list for the element using a binary search algorithm. If the element is not found, the inverted insertion point is returned.
     * @param li The sorted list to search
     * @return Return the index of the element, if it is contained in the list within the specified range; otherwise, the inverted insertion point (-insertion point - 1). The insertion point is defined as the index at which the element should be inserted, so that the list (or the specified subrange of list) still remains sorted.
     */
    public static int binarySearch(List<Double> li, double query) {
        var min = 0;
        var max = li.size() - 1;
        while (min != max) {
            var mid = (min + max) / 2;
            var midVal = li.get(mid);
            if (midVal < query) {
                min = mid;
            } else if (midVal > query) {
                max = mid;
            } else {
                return mid; // element found
            }
        }
        return -min - 1; // element not found
    }

    /**
     * precondition: source and target are sorted and share the same length
     */
    public static double lerpLookup(List<Double> source, List<Double> target, double query) {
        if (source.size() != target.size()) {
            throw new IllegalArgumentException("source and target must have the same length");
        }
        if (source.isEmpty()) {
            throw new IllegalArgumentException("source must be nonempty");
        }

        var index = binarySearch(source, query);
        if (index >= 0) {
            return target.get(index);
        } else {
            var insIndex = -(index + 1);
            if(insIndex <= 0) {
                return target.get(0);
            } else if(insIndex >= source.size()) {
                return target.get(target.size() - 1);
            } else {
                double sLo = source.get(insIndex - 1);
                double sHi = source.get(insIndex);
                double tLo = target.get(insIndex - 1);
                double tHi = target.get(insIndex);
                return lerp(query, sLo, sHi, tLo, tHi);
            }
        }
    }

    // precondition: source, target sorted and share the same length; queries sorted
    public static List<Double> lerpLookupMap(List<Double> source, List<Double> target, List<Double> queries) {
        if (source.size() != target.size()) {
            throw new IllegalArgumentException("source and target must have the same length");
        }
        if (source.isEmpty()) {
            throw new IllegalArgumentException("source must be nonempty");
        }
        var result = new ArrayList<Double>();
        var i = 0;
        for (Double query: queries) {
            if (query < source.get(0)) {
                result.add(target.get(0));
                continue;
            }

            while (i + 1 < source.size() && source.get(i + 1) < query) {
                i++;
            }

            if (i + 1 == source.size()) {
                result.add(target.get(target.size() - 1));
                continue;
            }

            var sLo = source.get(i);
            var sHi = source.get(i + 1);
            var tLo = target.get(i);
            var tHi = target.get(i + 1);
            result.add(lerp(query, sLo, sHi, tLo, tHi));
        }
        return result;
    }
}
