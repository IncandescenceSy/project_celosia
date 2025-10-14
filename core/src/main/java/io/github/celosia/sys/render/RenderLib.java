package io.github.celosia.sys.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import io.github.celosia.sys.save.Settings;

import static io.github.celosia.Main.viewPort;

public class RenderLib {

    public static final Color TRANS_BLACK = new Color(0, 0, 0, 0.6f);

    public static final int BAR_HP = 2;
    public static final int BAR_SHIELD = 1;
    public static final int BAR_OVERHEAL = 0;

    public static void changeScale(int x, int y) {
        viewPort.update(x, y, true);
        Gdx.graphics.setWindowedMode(x, y);
    }

    public static void changeScale(float scale) {
        changeScale((int) (2560 * scale), (int) (1440 * scale));
    }

    // Auto
    public static void changeScale() {
        Graphics.DisplayMode dispMode = Gdx.graphics.getDisplayMode();
        double scale = Math.min(dispMode.width / 16, dispMode.height / 9);
        changeScale((int) (16 * scale), (int) (9 * scale));
    }

    public static int getTargetFPS() {
        Graphics.DisplayMode dispMode = Gdx.graphics.getDisplayMode();
        if (Settings.useVsync || Settings.autoFps) return dispMode.refreshRate + 1;
        return Settings.targetFPS;
    }
}
