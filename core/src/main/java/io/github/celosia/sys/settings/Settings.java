package io.github.celosia.sys.settings;

public class Settings {
    // Display settings
    // Resolution relative to 2560x1440
    // Todo pull resolution options from easy-to-modify txt file
    public static float scale = 1f;

    // Whether the application is in fullscreen
    public static boolean isFullscreen = false;

    // Target FPS
    public static int targetFPS = 61;

    // Whether to use the monitor's refresh rate + 1 as Target FPS
    // Hidden from the user. User is instead presented with 1 "Target FPS" option with choices of 60, 120, 144, 165, 240, and Auto (and maybe some other options idk what people have)
    public static boolean useMonitor = true;

    // Whether to use VSync. Overrides Target FPS setting
    public static boolean useVsync = true;

    // Audio settings
    // Music volume (0-1)
    public static float volMus = 0.75f;

    // Sound effect volume (0-1)
    public static float volSfx = 0.75f;

    // Gameplay settings
    // Whether to show story scenes
    public static boolean showStory = true;

    // Whether to show a speedrun timer onscreen
    public static boolean speedrunTimer = false;

    // Difficulty (todo)
    // Per-savefile instead of game-wide
    public static int difficulty = 0;

    // Battle settings
    // Battle speed relative to 100%
    public static float battleSpeed = 1f;

    // Whether to display HP with the raw numbers or % for each side (only applies to the numbers on HP bars, not damage numbers)
    // todo would anyone actually use this
    public static boolean hpPercentAlly = false;
    public static boolean hpPercentOpponent = false;
}
