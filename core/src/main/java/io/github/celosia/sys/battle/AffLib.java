package io.github.celosia.sys.battle;

// Utilities for dealing with Affinity
public class AffLib {
	private static final int[] affMultDmgDealt = {3000, 5000, 6500, 8000, 9000, 10000, 11000, 12000, 13500, 15000,
			17000};
	private static final int[] affMultDmgTaken = {25000, 20000, 17000, 14000, 12000, 10000, 9000, 8000, 6500, 5000, 0};
	private static final int[] affMultSpCost = {17000, 15000, 13000, 12000, 11000, 10000, 9500, 9000, 8500, 8000, 7500};

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
