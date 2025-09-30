package io.github.celosia.lwjgl3;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.celosia.Main;
import io.github.celosia.sys.settings.Settings;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        // This handles macOS support and helps on Windows.
        if (StartupHelper.startNewJvmIfRequired()) {
            return;
        }

        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        Graphics.DisplayMode dispMode = Lwjgl3ApplicationConfiguration.getDisplayMode();

        configuration.setTitle("Project Celosia");

        //// Vsync limits the frames per second to what your hardware can display, and helps eliminate
        //// screen tearing. This setting doesn't always work on Linux, so the line after is a safeguard.
        configuration.useVsync(true);

        //// Limits FPS to the refresh rate of the currently active monitor, plus 1 to try to match fractional
        //// refresh rates. The Vsync setting above should limit the actual FPS to match the monitor.
        configuration.setForegroundFPS(dispMode.refreshRate + 1);

        //// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
        //// useful for testing performance, but can also be very stressful to some hardware.
        //// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.

        // Set resolution to display resolution
        double scale = Math.min(dispMode.width / 16, dispMode.height / 9);
        int y = (int) (9 * scale);
        configuration.setWindowedMode((int) (16 * scale), y);
        Settings.scale = y / 1440f;

        //// You can change these files; they are in lwjgl3/src/main/resources/ .
        //// They can also be loaded from the root of assets/ .
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");

        // Enable multisampling for better ShapeDrawer render quality. It doesn't seem like increasing samples beyond 1 improves quality
        configuration.setBackBufferConfig(8, 8, 8, 8, 16, 0, 1);

        // todo: disallow window resizing and provide a preset selection? resizing creates ugly black bars and flicker, and if i resize the window too much, it stops taking input for X amount of time

        return configuration;
    }
}
