package io.github.celosia.sys.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;

import static io.github.celosia.Main.viewPort;

public class RenderLib {

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
}
