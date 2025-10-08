package io.github.celosia.sys;

// Debug
public class Debug {

    // Enables debug hotkeys
    // Main menu:
    // Q: Open Debug menu
    // In battle:
    // Q: Prints battle log to console
    // W: Test popup
    public static boolean enableDebugHotkeys = false;

    // Shows some extra info
    public static boolean showDebugInfo = false;

    // A <-> B; X <-> Y
    // Emulates NSW controller. Results in incorrect glyphs when using non-NSW
    // controller
    public static boolean alwaysUseNSWLayout = false;

    // Select opponent moves
    // todo fix (doesnt work)
    public static boolean selectOpponentMoves = false;
}
