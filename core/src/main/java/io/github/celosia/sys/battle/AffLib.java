package io.github.celosia.sys.battle;

// Utilities for dealing with Affinity
public class AffLib {
    private static final double[] affMultDmgDealt = {0.3, 0.5, 0.65, 0.8, 0.9, 1, 1.1, 1.2, 1.35, 1.5, 1.7};
    private static final double[] affMultDmgTaken = {2.5, 2, 1.7, 1.4, 1.2, 1, 0.9, 0.8, 0.65, 0.5, 0};
    private static final double[] affMultSpCost = {1.7, 1.5, 1.3, 1.2, 1.1, 1, 0.95, 0.9, 0.85, 0.8, 0.75};

    public static double getAffMultDmgDealt(int aff) {
        return affMultDmgDealt[Math.clamp(aff, -5, 5) + 5];
    }

    public static double getAffMultDmgTaken(int aff) {
        return affMultDmgTaken[Math.clamp(aff, -5, 5) + 5];
    }

    public static double getAffMultSpCost(int aff) {
        return affMultSpCost[Math.clamp(aff, -5, 5) + 5];
    }
}
