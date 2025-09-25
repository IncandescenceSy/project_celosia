package io.github.celosia.sys.util;

/**
 * int array that can be indexed out of bounds, extrapolating new values based
 * on exact index and step vars
 *
 * @param core
 *            int array contained within
 * @param offset
 *            Indices to move core by
 * @param stepUp
 *            Amount to increase core[last] by per index after it
 * @param stepDown
 *            Amount to increase core[0] by per index before it
 */
public record ExtrapolatingArray(int[] core, int offset, int stepUp, int stepDown, boolean maxTo0) {
	public int get(int index) {
		// Real index
		int i = index + offset;

		// Value
		int value;

		// In bounds
		if (i >= 0 && i < core.length) {
			value = core[i];
		}

		// Above bounds; return core[last] + (stepUp * amount of indices above bounds)
		else if (i >= core.length) {
			value = core[core.length - 1] + (stepUp * i - (core.length - 1));
		}

		// Below bounds; return core[0] + (stepDown * amount of indices below bounds)
		else {
			value = core[0] + (stepDown * Math.abs(i));
		}

		if (maxTo0) {
			return Math.max(value, 0);
		} else {
			return value;
		}
	}
}
