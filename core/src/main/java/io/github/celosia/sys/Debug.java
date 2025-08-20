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
    public static boolean swapFaceButtons = false;

    // Select opponent moves
    // todo fix (doesnt work)
    public static boolean selectOpponentMoves = false;

    // Treat LT as axis 4 and RT as axis 5
    // If disabled, they're instead treated as buttons, which doesn't work on desktop
    public static boolean treatTriggersAsAxes = true;
}
