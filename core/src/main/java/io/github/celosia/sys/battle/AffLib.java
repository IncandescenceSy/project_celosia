package io.github.celosia.sys.battle;

// Utilities for dealing with Affinity
// todo above/below +/-5?
public class AffLib {
	private static final int[] affMultDmgDealt = {300, 500, 650, 800, 900, 1000, 1100, 1200, 1350, 1500, 1700};
	private static final int[] affMultDmgTaken = {2500, 2000, 1700, 1400, 1200, 1000, 900, 800, 650, 500, 0};
	private static final int[] affMultSpCost = {1700, 1500, 1300, 1200, 1100, 1000, 950, 900, 850, 800, 750};

	public static int getAffMultDmgDealt(int aff) {
		return affMultDmgDealt[Math.clamp(aff, -5, 5) + 5];
	}

	public static int getAffMultDmgTaken(int aff) {
		return affMultDmgTaken[Math.clamp(aff, -5, 5) + 5];
	}

	public static int getAffMultSpCost(int aff) {
		return affMultSpCost[Math.clamp(aff, -5, 5) + 5];
	}
}
