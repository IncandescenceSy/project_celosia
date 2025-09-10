package io.github.celosia.sys.settings;

public class Settings {
    // Gameplay settings

    // Speed of battle animations
    // Duration of in-battle pauses relative to 100% (1 = 100%, 0.1 = 10%)
    public static float battleSpeed = 1f;

    // Whether to display a warning prompt before selecting an invalid move
    // Invalid moves may have become valid by the time they execute, and if you're using strategies where this could happen often, you should probably disable this
    public static boolean showInvalidMoveWarning = true;

    // Whether to show a speedrun timer onscreen
    public static boolean showSpeedrunTimer = false;

    // Whether to show story scenes
    public static boolean showStory = true;

    // Whether to show a warning and the option to skip before showing story scenes that reference or depict self-harm, suicide, or drugs
    // Skipping these scenes will remove large parts of the game's story, and it will no longer make sense
    public static boolean showStoryWarning = false;


    // Display settings

    // Resolution relative to 2560x1440
    // Todo pull resolution options from easy-to-modify txt file
    public static float scale = 1f;

    // Whether the application is in fullscreen
    public static boolean isFullscreen = false;

    // Whether to use VSync. If true, Target FPS setting is ignored
    public static boolean useVsync = true;

    // Target FPS. Only used if Vsync is disabled
    public static int targetFPS = 60;

    // Whether to use the monitor's refresh rate + 1 as Target FPS
    // Hidden from the user. User is instead presented with 1 "Target FPS" option with choices of 60, 120, 144, 165, 240, and Auto (and maybe some other options idk what people have)
    public static boolean useMonitor = true;

    // Whether to show an FPS counter
    public static boolean showFpsCounter = false;


    // Audio settings

    // Music volume (0-1)
    public static float volMus = 0.75f;

    // Sound effect volume (0-1)
    public static float volSfx = 0.75f;


    // Control settings
    // todo remap controls for both KB and controller, swap A/B if language is Japanese and other countries that do that?

    // If true, shows a small guide to all currently possible inputs in the bottom left
    public static boolean showInputGuide = true;

    // If true, if a Nintendo controller is in use, swaps the Confirm and Back buttons to match Nintendo button layout
    public static boolean detectNintendoController = true;
}
