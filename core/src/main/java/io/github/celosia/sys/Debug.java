package io.github.celosia.sys;

// Debug
public class Debug {
	// Enables debug hotkeys
	// Q: Prints battle log to console
	public static boolean enableDebugHotkeys = false;

	// Shows some extra info
	public static boolean showDebugInfo = false;

	// A <-> B; X <-> Y
	// Emulates Nintendo controller
	public static boolean alwaysUseNintendoLayout = false;

	// Select opponent moves
	// todo fix (doesnt work)
	public static boolean selectOpponentMoves = false;

	// If true, display real stats instead of display stats scaled down by
	// STAT_FACTOR
	// May not work in all places
	// todo implement
	public static boolean displayRealStats = false;

	// Treat LT as axis 4 and RT as axis 5
	// If disabled, they're instead treated as buttons, which doesn't work on
	// desktop
	// You might think that it'd work with Nintendo controllers, which have binary
	// triggers, but it doesn't (presumably due to how Nintendo controller PC compat
	// tools work)
	// I haven't tried any other controller that has binary triggers
	public static boolean treatTriggersAsAxes = true;
}
