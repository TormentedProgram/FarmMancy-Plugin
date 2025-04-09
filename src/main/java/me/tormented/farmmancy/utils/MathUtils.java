package me.tormented.farmmancy.utils;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.TreeMap;

public final class MathUtils {
    private MathUtils() {}

    public static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Vector lerp(@NotNull Vector a, @NotNull Vector b, double t) {
        return new Vector(
                lerp(a.getX(), b.getX(), t),
                lerp(a.getY(), b.getY(), t),
                lerp(a.getZ(), b.getZ(), t)
        );
    }

    public static class LinearScaleGraph {

        private final TreeMap<Double, Double> points;

        public LinearScaleGraph() {
            points = new TreeMap<>();
        }

        public LinearScaleGraph(Map<? extends Double,  ? extends Double> m) {
            points = new TreeMap<>(m);
        }

        public void putPoint(double x, double y) {
            points.put(x, y);
        }


        public double getValue(double x) {

            if (points.isEmpty()) return 0;

            if (points.get(x) instanceof Double perfectValue)
                return perfectValue;

            Map.Entry<Double, Double> flooredEntry = points.floorEntry(x);
            Map.Entry<Double, Double> ceilingedEntry = points.ceilingEntry(x);

            if (flooredEntry != null && ceilingedEntry == null) {
                return flooredEntry.getValue();
            }
            if (flooredEntry == null && ceilingedEntry != null) {
                return ceilingedEntry.getValue();
            }
            if (flooredEntry == null && ceilingedEntry == null) {
                // This shouldn't happen
                return 0;
            }

            return findYBetweenTwoPoints(
                    flooredEntry.getKey(),
                    flooredEntry.getValue(),
                    ceilingedEntry.getKey(),
                    ceilingedEntry.getValue(),
                    x
            );


        }

    }
    public static double findYBetweenTwoPoints(double point1X, double point1Y, double point2X, double point2Y, double xValue) throws IllegalArgumentException {

        if (point1X == point2X)
            throw new IllegalArgumentException("X points cannot be the same");
        if (point1X > point2X)
            throw new IllegalArgumentException("The first X points must come before the second");
        if (xValue < point1X || xValue > point2X)
            throw new IllegalArgumentException("The testing X value must be in between the two X bounds");

        // Point gradient go BRRRRRR
        return ( (point2Y - point1Y)/(point2X - point1X) ) * (xValue - point1X) + point1Y;
    }

}
