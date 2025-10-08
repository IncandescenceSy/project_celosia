package io.github.celosia.sys.save;

public class Settings {

    //// Gameplay settings

    // Speed of battle animations
    // Duration of in-battle pauses relative to 100% (1 = 100%, 0.1 = 10%)
    public static float battleSpeed = 1f;

    // Whether to display a warning prompt before selecting an invalid move
    // Invalid moves may have become valid by the time they execute, and if you're
    // using strategies where this could happen often, you should probably disable
    // this
    public static boolean showInvalidMoveWarning = true;

    // Whether to show a speedrun timer onscreen
    public static boolean showSpeedrunTimer = false;

    // Whether to show story scenes
    public static boolean showStory = true;

    // Whether to show a warning and the option to skip before showing story scenes
    // that reference or depict self-harm, suicide, or drugs
    // Skipping these scenes will remove large parts of the game's story, and it
    // will no longer make sense
    public static boolean showStoryWarning = false;

    //// Display settings

    // Whether to ignore windowScale and use the nearest fitting 16:9 to the
    // display's resolution
    // Hidden from the user. User is instead presented with 1 "Resolution" option
    // with various choices and Auto
    // Steam hardware survey Aug 2025:
    // 54.44% 1920x1080, 20.19% 2560x1440, 5.09% 2560x1600, 4.59% 3840x2160, 2.85%
    // 3440x1440, 2.49% 1366x768, 1.80% 1920x1200
    public static boolean autoRes = true;

    // Whether the application is in fullscreen
    public static boolean isFullscreen = false;

    // Whether to use VSync. If true, Target FPS setting is ignored
    public static boolean useVsync = true;

    // Target FPS. Only used if Vsync is disabled
    public static int targetFPS = 60;

    // Whether to ignore targetFPS and use the display's refresh rate + 1 instead
    // Hidden from the user. User is instead presented with 1 "Target FPS" option
    // with choices of 60, 120, 144, 165, 240, 360, and Auto (and maybe some others
    // idk)
    public static boolean autoFps = true;

    // Whether to show an FPS counter
    public static boolean showFpsCounter = false;

    //// Audio settings

    // Music volume (0-1)
    public static float volMus = 0.75f;

    // Sound effect volume (0-1)
    public static float volSfx = 0.75f;

    // Control settings
    // todo remap controls for both KB and controller, swap A/B if language is
    // Japanese and other countries that do that?

    // If true, shows a small guide to all currently possible inputs in the bottom
    // left
    public static boolean showInputGuide = true;

    // If true, if a Nintendo controller is in use, swaps the Confirm and Back
    // buttons to match Nintendo button layout
    public static boolean detectNintendoController = true;
}
