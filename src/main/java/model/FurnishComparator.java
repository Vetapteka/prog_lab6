package model;

import java.util.Comparator;

/**
 * Comparator for {@link Furnish}
 */
public class FurnishComparator implements Comparator<Furnish> {
    /**
     * @param a {@link Furnish} to compare
     * @param b {@link Furnish} to compare
     * @return -1 if a is lower b, 0 if a is equals b, 1 if a is greater b
     */
    @Override
    public int compare(final Furnish a, final Furnish b) {
        return Integer.compare(a.ordinal(), b.ordinal());
    }

}
