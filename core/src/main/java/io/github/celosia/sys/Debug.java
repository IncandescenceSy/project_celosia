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

    // If true, display real stats instead of display stats scaled down by
    // STAT_MULT_HIDDEN
    // May not work in all places
    // todo implement
    public static boolean displayRealStats = false;

    // Treat LT as axis 4 and RT as axis 5
    // If disabled, they're instead treated as buttons, which doesn't work on
    // desktop
    // You might think that it'd work with NSW controllers, which have binary
    // triggers, but it doesn't (presumably due to how NSW controller PC compat
    // tools work)
    // I haven't tried any other controller that has binary triggers
    public static boolean treatTriggersAsAxes = true;

    // Generate bitmap fonts on startup instead of reading from file
    // Expects to find fnt/koruri.ttf
    public static boolean generateFonts = true;
}
