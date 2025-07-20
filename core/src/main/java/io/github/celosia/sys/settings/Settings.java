package io.github.celosia.sys.settings;

public class Settings {
    // Display settings
    // Resolution relative to 2560x1440
    public static float scale = 1f;

    // Whether the application is in fullscreen
    public static boolean isFullscreen = false;

    // Battle settings
    // Battle speed relative to 100%
    public static float battleSpeed = 1f;

    // Whether to display HP with the raw numbers or % for each side (only applies to the numbers on HP bars, not damage numbers)
    public static boolean hpPercentAlly = false;
    public static boolean hpPercentOpponent = false;
}
