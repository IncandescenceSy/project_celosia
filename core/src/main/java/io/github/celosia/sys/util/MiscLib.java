package io.github.celosia.sys.util;

public class MiscLib {
	// Returns true if a and b are both >= 1, and false otherwise
	public static boolean isMatchingTruthiness(double a, double b) {
		return a >= 1 && b >= 1;
	}

	public static int booleanToInt(boolean a) {
		return a ? 1 : 0;
	}
}
